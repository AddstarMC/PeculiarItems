package au.com.mineauz.peculiaritems.peculiarstats;

import java.util.Map;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Maps;

import au.com.mineauz.peculiaritems.PCRUtils;
import au.com.mineauz.peculiaritems.PCRUtils.ToolType;

public class MonstersKilledStat extends PeculiarStat implements SubStatable<EntityType> {
	private final Map<EntityType, MonsterTypeSubStat> substats = Maps.newHashMap();
	
	@Override
	public PeculiarSubStat of(EntityType type) {
		MonsterTypeSubStat stat = substats.get(type);
		if (stat == null) {
			stat = new MonsterTypeSubStat(this, type);
			substats.put(type, stat);
		}
		
		return stat;
	}
	
	@Override
	public PeculiarSubStat ofEncoded(String subtypeName) {
		try {
			EntityType type = EntityType.valueOf(subtypeName);
			return of(type);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
	
	@Override
	public String getName() {
		return "MONSTERS_KILLED";
	}

	@Override
	public ChatColor getDisplayColor() {
		return ChatColor.RED;
	}

	@Override
	public String getDisplayName() {
		return "Monsters Killed";
	}

	@Override
	public boolean isCompatibleItem(ItemStack item) {
		ToolType type = PCRUtils.getType(item);
		
		return (type == ToolType.Sword || type == ToolType.Axe);
	}
	
	private class MonsterTypeSubStat extends PeculiarSubStat {
		private final EntityType type;
		private String name;
		
		public MonsterTypeSubStat(PeculiarStat parent, EntityType type) {
			super(parent);
			this.type = type;
		}

		@Override
		public String getSubName() {
			return type.name();
		}

		@Override
		public String getDisplayName() {
			if (name == null) {
				name = type.name().replace('_', ' ');
				name = WordUtils.capitalizeFully(name);
			}
			
			return name;
		}
	}
}
