package aisi163.selfmod.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber
class RegisterDirtBall {
    public static final DirtBall dirt_ball_item = new DirtBall();

    @SubscribeEvent
    public static void MinecraftItemRegisterEventListener(Register<Item> item_register_event){
        /* 注册物品 */
        IForgeRegistry<Item> item_register_obj = item_register_event.getRegistry();
        item_register_obj.register(dirt_ball_item);
    }

    @SubscribeEvent
    public static void MinecraftLoadModelResourceEventListener(ModelRegistryEvent event){
        /* 将模型与物品绑定 */
        ModelResourceLocation load_model = new ModelResourceLocation("bcc16f35-fdfe-47b1-bc90-3cde64ba15b7:dirt_ball","inventory");
        ModelLoader.setCustomModelResourceLocation(dirt_ball_item, 0, load_model);
    }
}