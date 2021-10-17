package skyblockclient.events

import net.minecraft.network.Packet
import net.minecraftforge.fml.common.eventhandler.Cancelable
import net.minecraftforge.fml.common.eventhandler.Event

@Cancelable
class ReceivePacketEvent(var packet: Packet<*>) : Event()