package club.sk1er.mods.scrollabletooltips.transform.impl;

import club.sk1er.mods.scrollabletooltips.transform.TooltipsTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

public final class GuiUtilsTransformer implements TooltipsTransformer {

    @Override
    public String[] getClassNames() {
        return new String[]{"net.minecraftforge.fml.client.config.GuiUtils"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {

        for (MethodNode method : classNode.methods) {
            if (method.name.equals("drawHoveringText")) {
                int tooltipYIndex = -1;
                int tooltipHeightIndex = -1;

                for (LocalVariableNode variable : method.localVariables) {
                    if (variable.name.equals("tooltipY")) {
                        tooltipYIndex = variable.index;
                    } else if (variable.name.equals("tooltipHeight")) {
                        tooltipHeightIndex = variable.index;
                    }
                }

                final ListIterator<AbstractInsnNode> iterator = method.instructions.iterator();

                while (iterator.hasNext()) {
                    final AbstractInsnNode next = iterator.next();

                    if (next instanceof IntInsnNode && ((IntInsnNode) next).operand == 300 && next.getNext().getOpcode() == Opcodes.ISTORE) {
                        method.instructions.insertBefore(next, addScrollFunctionality(tooltipYIndex, tooltipHeightIndex));
                    } else if (next instanceof MethodInsnNode && next.getOpcode() == Opcodes.INVOKESTATIC) {
                        final String methodInsnName = mapMethodNameFromNode(next);

                        if (methodInsnName.equals("enableRescaleNormal") || methodInsnName.equals("func_179091_B")) {
                            method.instructions.insert(next, new MethodInsnNode(Opcodes.INVOKESTATIC,
                                    "net/minecraft/client/renderer/GlStateManager",
                                    "func_179121_F",
                                    "()V",
                                    false));
                        }
                    }
                }

                break;
            }
        }
    }

    private InsnList addScrollFunctionality(int tooltipYIndex, int tooltipHeightIndex) {
        InsnList insnList = new InsnList();
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 0)); // textLines
        insnList.add(new VarInsnNode(Opcodes.ILOAD, 4)); // screenHeight
        insnList.add(new VarInsnNode(Opcodes.ILOAD, tooltipYIndex));
        insnList.add(new VarInsnNode(Opcodes.ILOAD, tooltipHeightIndex));
        insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "club/sk1er/mods/scrollabletooltips/GuiUtilsOverride", "drawHoveringText", "(Ljava/util/List;III)V", false));
        return insnList;
    }
}
