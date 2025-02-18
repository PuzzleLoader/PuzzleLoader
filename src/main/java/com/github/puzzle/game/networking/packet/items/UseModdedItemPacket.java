package com.github.puzzle.game.networking.packet.items;

import com.github.puzzle.game.items.IModItem;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.items.ItemSlot;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.networking.GamePacket;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import finalforeach.cosmicreach.networking.NetworkSide;
import finalforeach.cosmicreach.world.Zone;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class UseModdedItemPacket extends GamePacket {
	int selectedSlotNum;
	BlockPosition targetPlaceBlockPos;
	BlockPosition targetBreakBlockPos;
	boolean isLeftClick;

	public UseModdedItemPacket() {
	}

	public UseModdedItemPacket(int selectedSlotNum, BlockPosition targetPlaceBlockPos, BlockPosition targetBreakBlockPos, boolean isLeftClick) {
		this.selectedSlotNum = selectedSlotNum;
		this.targetPlaceBlockPos = targetPlaceBlockPos;
		this.targetBreakBlockPos = targetBreakBlockPos;
		this.isLeftClick = isLeftClick;
	}

	@Override
	public void receive(ByteBuf in) {
		this.selectedSlotNum = this.readInt(in);
		this.targetPlaceBlockPos = this.readBlockPositionZoneless(in);
		this.targetBreakBlockPos = this.readBlockPositionZoneless(in);
		this.isLeftClick = this.readBoolean(in);
	}

	@Override
	public void write() {
		this.writeInt(this.selectedSlotNum);
		if (this.targetPlaceBlockPos != null) {
			this.writeBlockPosition(this.targetPlaceBlockPos);
		}
		if (this.targetBreakBlockPos != null) {
			this.writeBlockPosition(this.targetBreakBlockPos);
		}
		this.writeBoolean(this.isLeftClick);
	}

	@Override
	public void handle(NetworkIdentity identity, ChannelHandlerContext ctx) {
		if (identity.getSide() != NetworkSide.CLIENT) {
			Player player = identity.getPlayer();
			Zone zone = identity.getZone();
			ItemSlot itemslot = player.inventory.getSlot(this.selectedSlotNum);
			ItemStack itemstack = itemslot != null ? itemslot.getItemStack() : null;
			if (this.targetPlaceBlockPos != null) this.targetPlaceBlockPos.convertToLocal(zone);
			if (this.targetBreakBlockPos != null) this.targetBreakBlockPos.convertToLocal(zone);

			if (itemstack != null && itemstack.getItem() instanceof IModItem modItem) {
				modItem.use(itemslot, player, this.targetPlaceBlockPos, this.targetBreakBlockPos, this.isLeftClick);
			}
		}
	}
}
