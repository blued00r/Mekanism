package mekanism.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import mekanism.api.ITileNetwork;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.server.FMLServerHandler;

/**
 * Mekanism packet handler. As always, use packets sparingly!
 * @author AidanBrady
 *
 */
public class PacketHandler implements IPacketHandler
{
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		ByteArrayDataInput dataStream = ByteStreams.newDataInput(packet.data);
        EntityPlayer entityplayer = (EntityPlayer)player;
        
		if(packet.channel.equals("Mekanism"))
		{
			try {
				int packetType = dataStream.readInt();
				
			    if(packetType == EnumPacketType.TIME.id)
			    {
			        System.out.println("[Mekanism] Received time update packet from " + entityplayer.username + ".");
			        MekanismUtils.setHourForward(FMLServerHandler.instance().getServer().worldServerForDimension(0), dataStream.readInt());
			    }
			    if(packetType == EnumPacketType.WEATHER.id)
			    {
			    	System.out.println("[Mekanism] Received weather update packet from " + entityplayer.username + ".");
			    	int weatherType = dataStream.readInt();
			    	if(weatherType == EnumWeatherType.CLEAR.id)
			    	{
			    		entityplayer.worldObj.getWorldInfo().setRaining(false);
				        entityplayer.worldObj.getWorldInfo().setThundering(false);
			    	}
			    	if(weatherType == EnumWeatherType.HAZE.id)
			    	{
			    		entityplayer.worldObj.getWorldInfo().setRaining(true);
				        entityplayer.worldObj.getWorldInfo().setThundering(true);
			    	}
			    	if(weatherType == EnumWeatherType.RAIN.id)
			    	{
			    		entityplayer.worldObj.getWorldInfo().setRaining(true);
			    	}
			    	if(weatherType == EnumWeatherType.STORM.id)
			    	{
				    	entityplayer.worldObj.getWorldInfo().setThundering(true);
			    	}
			    }
			    if(packetType == EnumPacketType.TILE_ENTITY.id)
			    {
			    	try {
						int x = dataStream.readInt();
						int y = dataStream.readInt();
						int z = dataStream.readInt();
						
						World world = ((EntityPlayer)player).worldObj;
						TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
						if(tileEntity instanceof ITileNetwork)
						{
							((ITileNetwork)tileEntity).handlePacketData(manager, packet, ((EntityPlayer)player), dataStream);
						}
					} catch (Exception e)
					{
						System.err.println("[Mekanism] Error while handling tile entity packet.");
						e.printStackTrace();
					}
			    }
			}
			catch (Exception e)
			{
				System.err.println("[Mekanism] Error while handling data int packet.");
				e.printStackTrace();
			}
		}
		else if(packet.channel.equals("MekanismUE"))
		{
			
		}
	}
	
	/**
	 * Send an electric machine update packet from server to client. This will send the data int '4,' as well as the machine's
	 * x, y, and z coordinates, along with it's facing, active state, operating ticks, energy stored, max energy and ticks required.
	 * @param sender - tile entity who is sending the packet
	 */
	public static void sendElectricMachinePacket(TileEntityElectricMachine sender)
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(bytes);
        
        try {
        	output.writeInt(EnumPacketType.TILE_ENTITY.id);
        	output.writeInt(sender.xCoord);
        	output.writeInt(sender.yCoord);
        	output.writeInt(sender.zCoord);
        	output.writeInt(sender.facing);
        	output.writeByte(sender.isActive ? 1 : 0);
        	output.writeInt(sender.operatingTicks);
        	output.writeInt(sender.energyStored);
        	output.writeInt(sender.currentMaxEnergy);
        	output.writeInt(sender.currentTicksRequired);
        } catch (IOException e)
        {
        	System.err.println("[Mekanism] Error while writing tile entity packet.");
        	e.printStackTrace();
        }
        
        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "Mekanism";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        
        if(FMLCommonHandler.instance().getMinecraftServerInstance() != null)
        {
        	FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().sendPacketToAllPlayers(packet);
        }
	}
	
	/**
	 * Send an electric machine update packet from server to client with a defined range. This will send the data int '4,' as well as the machine's
	 * x, y, and z coordinates, along with it's facing, active state, operating ticks, energy stored, max energy and ticks required.
	 * @param sender - tile entity who is sending the packet
	 * @param distance - radius to send packet in
	 */
	public static void sendElectricMachinePacketWithRange(TileEntityElectricMachine sender, double distance)
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(bytes);
        
        try {
        	output.writeInt(EnumPacketType.TILE_ENTITY.id);
        	output.writeInt(sender.xCoord);
        	output.writeInt(sender.yCoord);
        	output.writeInt(sender.zCoord);
        	output.writeInt(sender.facing);
        	output.writeByte(sender.isActive ? 1 : 0);
        	output.writeInt(sender.operatingTicks);
        	output.writeInt(sender.energyStored);
        	output.writeInt(sender.currentMaxEnergy);
        	output.writeInt(sender.currentTicksRequired);
        } catch (IOException e)
        {
        	System.err.println("[Mekanism] Error while writing tile entity packet.");
        	e.printStackTrace();
        }
        
        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "Mekanism";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        
		PacketDispatcher.sendPacketToAllAround(sender.xCoord, sender.yCoord, sender.zCoord, distance, sender.worldObj.provider.dimensionId, packet);
	}
	
	/**
	 * Send an advanced electric machine update packet from server to client. This will send the data int '4,' as well as the machine's
	 * x, y, and z coordinates, along with it's facing, active state, operating ticks, energy stored, secondary energy stored, max energy 
	 * and ticks required.
	 * @param sender - tile entity who is sending the packet
	 */
	public static void sendAdvancedElectricMachinePacket(TileEntityAdvancedElectricMachine sender)
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(bytes);
        
        try {
        	output.writeInt(EnumPacketType.TILE_ENTITY.id);
        	output.writeInt(sender.xCoord);
        	output.writeInt(sender.yCoord);
        	output.writeInt(sender.zCoord);
        	output.writeInt(sender.facing);
        	output.writeByte(sender.isActive ? 1 : 0);
        	output.writeInt(sender.operatingTicks);
        	output.writeInt(sender.energyStored);
        	output.writeInt(sender.secondaryEnergyStored);
        	output.writeInt(sender.currentMaxEnergy);
        	output.writeInt(sender.currentTicksRequired);
        } catch (IOException e)
        {
        	System.err.println("[Mekanism] Error while writing tile entity packet.");
        	e.printStackTrace();
        }
        
        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "Mekanism";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        
        if(FMLCommonHandler.instance().getMinecraftServerInstance() != null)
        {
        	FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().sendPacketToAllPlayers(packet);
        }
	}
	
	/**
	 * Send an advanced electric machine update packet from server to client with a defined distance. This will send the data int '4,' as 
	 * well as the machine's x, y, and z coordinates, along with it's facing, active state, operating ticks, energy stored, secondary energy 
	 * stored, max energy and ticks required.
	 * @param sender - tile entity who is sending the packet
	 * @param distance - radius to send packet in
	 */
	public static void sendAdvancedElectricMachinePacketWithRange(TileEntityAdvancedElectricMachine sender, double distance)
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(bytes);
        
        try {
        	output.writeInt(EnumPacketType.TILE_ENTITY.id);
        	output.writeInt(sender.xCoord);
        	output.writeInt(sender.yCoord);
        	output.writeInt(sender.zCoord);
        	output.writeInt(sender.facing);
        	output.writeByte(sender.isActive ? 1 : 0);
        	output.writeInt(sender.operatingTicks);
        	output.writeInt(sender.energyStored);
        	output.writeInt(sender.secondaryEnergyStored);
        	output.writeInt(sender.currentMaxEnergy);
        	output.writeInt(sender.currentTicksRequired);
        } catch (IOException e)
        {
        	System.err.println("[Mekanism] Error while writing tile entity packet.");
        	e.printStackTrace();
        }
        
        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "Mekanism";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        
		PacketDispatcher.sendPacketToAllAround(sender.xCoord, sender.yCoord, sender.zCoord, distance, sender.worldObj.provider.dimensionId, packet);
	}
	
	/**
	 * Send a power unit packet from server to client. This will send the data int '4,' as well as the machine's x, y, and z
	 * coordinates, along with it's facing and energy stored.
	 * @param sender - tile entity who is sending the packet
	 */
	public static void sendPowerUnitPacket(TileEntityPowerUnit sender)
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(bytes);
        
        try {
        	output.writeInt(EnumPacketType.TILE_ENTITY.id);
        	output.writeInt(sender.xCoord);
        	output.writeInt(sender.yCoord);
        	output.writeInt(sender.zCoord);
        	output.writeInt(sender.facing);
        	output.writeInt(sender.energyStored);
        } catch (IOException e)
        {
        	System.err.println("[Mekanism] Error while writing tile entity packet.");
        	e.printStackTrace();
        }
        
        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "Mekanism";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        
        if(FMLCommonHandler.instance().getMinecraftServerInstance() != null)
        {
        	FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().sendPacketToAllPlayers(packet);
        }
	}
	
	/**
	 * Send a power unit packet from server to client with a defined range. This will send the data int '4,' as well as 
	 * the machine's x, y, and z coordinates, along with it's facing and energy stored.
	 * @param sender - tile entity who is sending the packet
	 * @param distance - radius to send packet in
	 */
	public static void sendPowerUnitPacketWithRange(TileEntityPowerUnit sender, double distance)
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(bytes);
        
        try {
        	output.writeInt(EnumPacketType.TILE_ENTITY.id);
        	output.writeInt(sender.xCoord);
        	output.writeInt(sender.yCoord);
        	output.writeInt(sender.zCoord);
        	output.writeInt(sender.facing);
        	output.writeInt(sender.energyStored);
        } catch (IOException e)
        {
        	System.err.println("[Mekanism] Error while writing tile entity packet.");
        	e.printStackTrace();
        }
        
        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "Mekanism";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        
        PacketDispatcher.sendPacketToAllAround(sender.xCoord, sender.yCoord, sender.zCoord, distance, sender.worldObj.provider.dimensionId, packet);
	}
	
	/**
	 * Send a generator packet from server to client. This will send the data int '4,' as well as the machine's x, y, and z
	 * coordinates, along with it's facing, energy stored, and fuel stored.
	 * @param sender - tile entity who is sending the packet
	 */
	public static void sendGeneratorPacket(TileEntityGenerator sender)
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(bytes);
        
        try {
        	output.writeInt(EnumPacketType.TILE_ENTITY.id);
        	output.writeInt(sender.xCoord);
        	output.writeInt(sender.yCoord);
        	output.writeInt(sender.zCoord);
        	output.writeInt(sender.facing);
        	output.writeInt(sender.energyStored);
        	output.writeInt(sender.fuelStored);
        } catch (IOException e)
        {
        	System.err.println("[Mekanism] Error while writing tile entity packet.");
        	e.printStackTrace();
        }
        
        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "Mekanism";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        
        if(FMLCommonHandler.instance().getMinecraftServerInstance() != null)
        {
        	FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().sendPacketToAllPlayers(packet);
        }
	}
	
	/**
	 * Send a generator packet from server to client with a defined range. This will send the data int '4,' as well as 
	 * the machine's x, y, and z coordinates, along with it's facing, energy stored, and fuel stored.
	 * @param sender - tile entity who is sending the packet
	 * @param distance - radius to send packet in
	 */
	public static void sendGeneratorPacketWithRange(TileEntityGenerator sender, double distance)
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(bytes);
        
        try {
        	output.writeInt(EnumPacketType.TILE_ENTITY.id);
        	output.writeInt(sender.xCoord);
        	output.writeInt(sender.yCoord);
        	output.writeInt(sender.zCoord);
        	output.writeInt(sender.facing);
        	output.writeInt(sender.energyStored);
        	output.writeInt(sender.fuelStored);
        } catch (IOException e)
        {
        	System.err.println("[Mekanism] Error while writing tile entity packet.");
        	e.printStackTrace();
        }
        
        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "Mekanism";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        
        PacketDispatcher.sendPacketToAllAround(sender.xCoord, sender.yCoord, sender.zCoord, distance, sender.worldObj.provider.dimensionId, packet);
	}
	
	/**
	 * Sends the server the defined packet data int.
	 * @param type - packet type
	 * @param i - int to send
	 */
	public static void sendPacketDataInt(EnumPacketType type, int i)
	{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);
        try {
        	data.writeInt(type.id);
			data.writeInt(i);
		} catch (IOException e) {
			System.out.println("[Mekanism] An error occured while writing packet data.");
			e.printStackTrace();
		}
        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "Mekanism";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        PacketDispatcher.sendPacketToServer(packet);
        System.out.println("[Mekanism] Sent data int packet '" + i + "' to server");
	}
}
