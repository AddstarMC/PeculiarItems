package au.com.mineauz.peculiaritems;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import au.com.mineauz.peculiaritems.peculiarstats.PeculiarStat;

public class PCRPlayer {
	
	private PeculiarItem activeItem = null;
	private PeculiarItem[] activeArmor = new PeculiarItem[4];
	private Player player;
	
	/**
	 * Creates a new Player for this plugin (automatic on join/quit).
	 * @param player 
	 */
	public PCRPlayer(Player player){
		this.player = player;
		
		//Checks if the active item is peculiar
		if(PCRUtils.isPeculiarItem(getItemInHand()))
			activeItem = new PeculiarItem(getItemInHand());
		
		//Checks if armor is peculiar
		updatePeculiarArmor();
	}
	
	/**
	 * Sends the player a message.
	 * @param message The message to send.
	 */
	public void sendMessage(String message){
		player.sendMessage(message);
	}
	
	/**
	 * Gets the players display name.
	 * @return The display name.
	 */
	public String getDisplayName(){
		return player.getDisplayName();
	}
	
	/**
	 * Gets the active {@link PeculiarItem} on this player.
	 * @return The {@link PeculiarItem} or <code>null</code> if no item is active.
	 */
	public PeculiarItem getActiveItem(){
		return activeItem;
	}
	
	/**
	 * Checks if this player has an active {@link PeculiarItem}
	 * @return <code>true</code> if they have an active item.
	 */
	public boolean hasActiveItem(){
		return activeItem != null;
	}
	
	/**
	 * Sets the active {@link PeculiarItem}. Checks to see if the item is actually peculiar 
	 * must be done before assigning one.
	 * @param item The new {@link PeculiarItem}.
	 */
	public void setActiveItem(PeculiarItem item){
		activeItem = item;
	}
	
	/**
	 * Increments the active {@link PeculiarItem} stat by a specific amount.
	 * @param stat The stat to increment.
	 * @param amount The amount to increment the stat by.
	 * @param special The substat that can be incremented.
	 */
	public void incrementActiveItemStat(String stat, int amount, String special){
		if(hasActiveItem() && getActiveItem().hasStat(stat)){
			PeculiarStat pstat = getActiveItem().getStat(stat);
			pstat.incrementStat(this, getActiveItem(), amount);
			pstat.callSubStat(activeItem, amount, special);
		}
	}
	
	/**
	 * Gets the active {@link PeculiarItem} in an armor slot.
	 * @param type The {@link ArmorType}
	 * @return The {@link PeculiarItem} in the armor slot or <code>null</code> if one isn't active.
	 */
	public PeculiarItem getActiveArmor(ArmorType type){
		return activeArmor[type.getArmorSlot()];
	}
	
	/**
	 * Sets the active {@link PeculiarItem} in an armor slot. Checks to see if the item is actually peculiar 
	 * must be done before assigning one.
	 * @param type The {@link ArmorType} of this item.
	 * @param item The {@link PeculiarItem}.
	 */
	public void setActiveArmor(ArmorType type, PeculiarItem item){
		activeArmor[type.getArmorSlot()] = item;
	}
	
	/**
	 * Checks if this player has an active {@link PeculiarItem} in the armor slot.
	 * @param type The {@link ArmorType} of this item.
	 * @return <code>true</code> if they have an active {@link PeculiarItem} in the armor slot.
	 */
	public boolean hasActiveArmor(ArmorType type){
		return getActiveArmor(type) != null;
	}
	
	/**
	 * Checks all armor in the players inventory to see if its peculiar,
	 * then adds it to active armor.
	 */
	public void updatePeculiarArmor(){
		int c = 0;
		for(ItemStack i : getArmorContents()){
			if(PCRUtils.isPeculiarItem(i))
				setActiveArmor(ArmorType.getArmorType(c), new PeculiarItem(getArmorContents()[c]));
			else
				setActiveArmor(ArmorType.getArmorType(c), null);
			
			c++;
		}
	}
	
	/**
	 * Increments all armor peices with the specific stat by a specific amount.
	 * @param stat The stat to increment.
	 * @param amount The amount to increment the stat by.
	 * @param special The substat that can be incremented.
	 */
	public void incrementActiveArmorStat(String stat, int amount, String special){
		for(ArmorType type : ArmorType.values()){
			if(hasActiveArmor(type) && getActiveArmor(type).hasStat(stat)){
				PeculiarStat pstat = getActiveArmor(type).getStat(stat);
				pstat.incrementStat(this, getActiveArmor(type), amount);
				pstat.callSubStat(getActiveArmor(type), amount, special);
			}
		}
	}
	
	/**
	 * Gets the Bukkit {@link Player}.
	 * @return the {@link Player}.
	 */
	public Player getPlayer(){
		return player;
	}
	
	/**
	 * Gets the {@link ItemStack} of the item in hand.
	 * @return The {@link ItemStack}.
	 */
	public ItemStack getItemInHand(){
		return player.getItemInHand();
	}
	
	/**
	 * Gets the {@link ItemStack} armor contents.
	 * @return The {@link ItemStack}[] of the armor contents.
	 */
	public ItemStack[] getArmorContents(){
		return player.getInventory().getArmorContents();
	}
	
	/**
	 * Gets an item in the players inventory.
	 * @param slot The slot the item is in.
	 * @return The {@link ItemStack} of that slot.
	 */
	public ItemStack getItem(int slot){
		return player.getInventory().getItem(slot);
	}
	
	public enum ArmorType {
		HELMET(0),
		CHESTPLATE(1),
		LEGGINGS(2),
		BOOTS(3);
		
		private final int value;
		private ArmorType(int val){
			this.value = val;
		}
		
		public int getArmorSlot(){
			return value;
		}
		
		/**
		 * Gets the armor type from the slot ID.
		 * @param slot The slot id (for armor)
		 * @return The {@link ArmorType} enum.
		 */
		public static ArmorType getArmorType(int slot){
			if(slot == 0)
				return HELMET;
			else if(slot == 1)
				return CHESTPLATE;
			else if(slot == 2)
				return LEGGINGS;
			else if(slot == 3)
				return BOOTS;
			return null;
		}
	}
}