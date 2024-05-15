package aisi163.selfmod.item;
import net.minecraft.item.Item;

class DirtBall extends Item {
    public DirtBall() {
        // 设置物品的唯一ID标识符
        this.setRegistryName("dirt_ball");
        // 设置物品的未初始化名称
        this.setUnlocalizedName(Main.Mod_ID + "." + "dirt_ball");
        // 设置物品在存储槽中的最大存储数量
        this.setMaxStackSize(16);
        // 设置物品的最大耐久 (在底层代码中,耐久度是随玩家使用进行递增,如果耐久度 > 最大耐久度,物品损坏)
        this.setMaxDamage(12000);
    }
};