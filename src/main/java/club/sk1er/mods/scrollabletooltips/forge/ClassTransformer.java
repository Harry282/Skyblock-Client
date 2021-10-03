package club.sk1er.mods.scrollabletooltips.forge;

import club.sk1er.mods.scrollabletooltips.transform.TooltipsTransformer;
import club.sk1er.mods.scrollabletooltips.transform.impl.GuiUtilsTransformer;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

public final class ClassTransformer implements IClassTransformer {

    public final boolean outputBytecode = Boolean.parseBoolean(System.getProperty("debugBytecode", "false"));
    private final Logger logger = LogManager.getLogger("Tooltips Transformer");
    private final Multimap<String, TooltipsTransformer> transformerMap = ArrayListMultimap.create();

    public ClassTransformer() {
        this.registerTransformer(new GuiUtilsTransformer());
    }

    private void registerTransformer(TooltipsTransformer transformer) {
        for (String cls : transformer.getClassNames()) {
            this.transformerMap.put(cls, transformer);
        }
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (bytes == null) return null;

        Collection<TooltipsTransformer> transformers = this.transformerMap.get(transformedName);
        if (transformers.isEmpty()) return bytes;

        logger.info("Found {} transformers for {}", transformers.size(), transformedName);

        ClassReader classReader = new ClassReader(bytes);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, ClassReader.EXPAND_FRAMES);

        for (TooltipsTransformer transformer : transformers) {
            logger.info("Applying transformer {} on {}...", transformer.getClass().getName(), transformedName);
            transformer.transform(classNode, transformedName);
        }

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

        try {
            classNode.accept(classWriter);
        } catch (Throwable e) {
            logger.error("Exception when transforming {} : {}", transformedName, e.getClass().getSimpleName(), e);
        }

        if (outputBytecode) {
            File bytecodeDirectory = new File("bytecode");
            String transformedClassName;

            // anonymous classes
            if (transformedName.contains("$")) {
                transformedClassName = transformedName.replace('$', '.') + ".class";
            } else {
                transformedClassName = transformedName + ".class";
            }

            File bytecodeOutput = new File(bytecodeDirectory, transformedClassName);

            try (FileOutputStream os = new FileOutputStream(bytecodeOutput)) {
                os.write(classWriter.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return classWriter.toByteArray();
    }

}
