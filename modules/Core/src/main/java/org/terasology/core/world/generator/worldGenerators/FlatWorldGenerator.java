/*
 * Copyright 2013 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.core.world.generator.worldGenerators;

import com.google.common.base.Optional;
import org.terasology.core.world.generator.facetProviders.BiomeProvider;
import org.terasology.core.world.generator.facetProviders.FlatSurfaceHeightProvider;
import org.terasology.core.world.generator.facetProviders.FloraProvider;
import org.terasology.core.world.generator.facetProviders.PerlinHumidityProvider;
import org.terasology.core.world.generator.facetProviders.PerlinSurfaceTemperatureProvider;
import org.terasology.core.world.generator.facetProviders.SeaLevelProvider;
import org.terasology.core.world.generator.facetProviders.SurfaceToDensityProvider;
import org.terasology.core.world.generator.facetProviders.TreeProvider;
import org.terasology.core.world.generator.rasterizers.FloraRasterizer;
import org.terasology.core.world.generator.rasterizers.SolidRasterizer;
import org.terasology.core.world.generator.rasterizers.TreeRasterizer;
import org.terasology.engine.SimpleUri;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.World;
import org.terasology.world.generation.WorldBuilder;
import org.terasology.world.generator.RegisterWorldGenerator;
import org.terasology.world.generator.WorldConfigurator;
import org.terasology.world.generator.WorldGenerator;

@RegisterWorldGenerator(id = "flat", displayName = "Flat")
public class FlatWorldGenerator implements WorldGenerator {

    private final SimpleUri uri;
    private String worldSeed;
    private World world;

    public FlatWorldGenerator(SimpleUri uri) {
        this.uri = uri;
    }

    @Override
    public final SimpleUri getUri() {
        return uri;
    }

    @Override
    public void setWorldSeed(final String seed) {
        worldSeed = seed;

        BlockManager blockManager = CoreRegistry.get(BlockManager.class);
        world = new WorldBuilder(worldSeed.hashCode())
                .addProvider(new SeaLevelProvider(32))
                        // height of 40 so that it is far enough from sea level so that it doesnt just create beachfront
                .addProvider(new FlatSurfaceHeightProvider(40))
                .addProvider(new PerlinHumidityProvider())
                .addProvider(new PerlinSurfaceTemperatureProvider())
                .addProvider(new BiomeProvider())
                .addProvider(new SurfaceToDensityProvider())
                .addProvider(new FloraProvider())
                .addProvider(new TreeProvider())
                .addRasterizer(new FloraRasterizer())
                .addRasterizer(new TreeRasterizer())
                .addRasterizer(new SolidRasterizer())
                .build();
    }

    @Override
    public void initialize() {
        world.initialize();
    }

    @Override
    public void createChunk(CoreChunk chunk) {
        world.rasterizeChunk(chunk);
    }

    @Override
    public Optional<WorldConfigurator> getConfigurator() {
        return Optional.absent();
    }

    @Override
    public World getWorld() {
        return world;
    }
}
