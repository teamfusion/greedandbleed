package com.github.teamfusion.greedandbleed.common.registry;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.entity.brain.sensor.PygmySpecificSensor;
import com.github.teamfusion.greedandbleed.platform.CoreRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class SensorRegistry {
    public static final CoreRegistry<SensorType<?>> SENSOR_TYPES = CoreRegistry.of(BuiltInRegistries.SENSOR_TYPE, GreedAndBleed.MOD_ID);

    public static final Supplier<SensorType<PygmySpecificSensor>> PYGMY_SPECIFIC_SENSOR = register("pygmy_specific_sensor", PygmySpecificSensor::new);

    @NotNull
    private static <U extends Sensor<?>> Supplier<SensorType<U>> register(String key, Supplier<U> sensorSupplier) {
        return SENSOR_TYPES.create(key, () -> new SensorType<>(sensorSupplier));
    }

}
