package au.com.mineauz.peculiaritems.peculiarstats;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.google.common.collect.Maps;

import au.com.addstar.monolith.StringTranslator;

public class BlocksBrokenStat extends PeculiarStat implements SubStatable<MaterialData> {
	private Map<MaterialData, MaterialSubStat> materialStats = Maps.newHashMap();
	
	@Override
	public PeculiarSubStat of(MaterialData type) {
		MaterialSubStat stat = materialStats.get(type);
		if (stat == null) {
			stat = new MaterialSubStat(this, type);
			materialStats.put(type, stat);
		}
		
		return stat;
	}
	
	@Override
	public PeculiarSubStat ofEncoded(String subtypeName) {
		String[] parts = subtypeName.split("_");
		if (parts.length != 2) {
			return null;
		}
		
		Material type = Material.getMaterial(parts[0].toUpperCase());
		if (type == null) {
			return null;
		}
		
		byte data;
		try {
			data = Byte.parseByte(parts[1]);
		} catch (NumberFormatException e) {
			return null;
		}
		
		return of(type.getNewData(data));
	}
	
	@Override
	public String getName() {
		return "BLOCKS_BROKEN";
	}

	@Override
	public ChatColor getDisplayColor() {
		return ChatColor.LIGHT_PURPLE;
	}

	@Override
	public String getDisplayName() {
		return "Blocks Broken";
	}

	@Override
	public boolean isCompatibleItem(String type) {
		if(type.equals("PICKAXE") || type.equals("AXE") || type.equals("SPADE"))
			return true;
		return false;
	}
	
	private class MaterialSubStat extends PeculiarSubStat {
		private final ItemStack type;
		
		public MaterialSubStat(BlocksBrokenStat parent, MaterialData type) {
			super(parent);
			this.type = new ItemStack(type.getItemType(), 1, type.getData());
		}
		
		@Override
		public String getDisplayName() {
			return StringTranslator.getName(type);
		}
		
		@Override
		public String getSubName() {
			return type.getType().name() + "_" + type.getDurability();
		}
	}
}
