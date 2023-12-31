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

package gay.pyrrha.wingit.block

import gay.pyrrha.wingit.WingIt
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.MapColor
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemGroups
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object ModBlocks {

    val COURSE_CORE: Block by lazy {
        CourseCoreBlock(
            FabricBlockSettings.create()
                .luminance(5)
                .mapColor(MapColor.MAGENTA)
                .dropsNothing()
                .requiresTool()
                .strength(-1f, 3600000f)
        )
    }

    fun init() {
        register(COURSE_CORE, "course_core")

        // do last
        initItemGroups()
    }

    private fun initItemGroups() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register {
            it.add(COURSE_CORE)
        }
    }

    private fun register(block: Block, path: String) {
        Registry.register(Registries.BLOCK, WingIt.id(path), block)
        Registry.register(Registries.ITEM, WingIt.id(path), BlockItem(block, FabricItemSettings()))
    }
}