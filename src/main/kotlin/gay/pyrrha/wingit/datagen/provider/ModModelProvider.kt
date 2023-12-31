/*
 * Copyright 2023 Pyrrha Wills
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gay.pyrrha.wingit.datagen.provider

import gay.pyrrha.wingit.block.ModBlocks
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.TexturedModel

class ModModelProvider(output: FabricDataOutput) : FabricModelProvider(output) {
    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        generator.registerItemModel(ModBlocks.COURSE_CORE)

        val courseCoreModelId =
            TexturedModel.CARPET.get(ModBlocks.COURSE_CORE).upload(ModBlocks.COURSE_CORE, generator.modelCollector)
        generator.blockStateCollector.accept(
            BlockStateModelGenerator.createSingletonBlockState(
                ModBlocks.COURSE_CORE,
                courseCoreModelId
            )
        )

    }

    override fun generateItemModels(generator: ItemModelGenerator) {

    }
}