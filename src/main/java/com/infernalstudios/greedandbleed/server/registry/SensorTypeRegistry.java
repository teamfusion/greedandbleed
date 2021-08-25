package com.infernalstudios.greedandbleed.server.registry;

import com.infernalstudios.greedandbleed.GreedAndBleed;
import com.infernalstudios.greedandbleed.server.sensor.PigmySpecificSensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SensorTypeRegistry {

    public static final DeferredRegister<SensorType<?>> SENSOR_TYPES = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, GreedAndBleed.MOD_ID);

    public static final RegistryObject<SensorType<PigmySpecificSensor>> PIGMY_SPECIFIC_SENSOR = SENSOR_TYPES.register(
            "pigmy_specific_sensor", () ->
                    new SensorType<>(PigmySpecificSensor::new)
    );
}
