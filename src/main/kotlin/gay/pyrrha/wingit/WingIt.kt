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

package gay.pyrrha.wingit

import gay.pyrrha.wingit.block.ModBlocks
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory

object WingIt : ModInitializer {
	const val MOD_ID: String = "wingit"

    private val logger = LoggerFactory.getLogger("WingIt")

	override fun onInitialize() {
		logger.info("Trans Rights!")

		ModBlocks.init()
	}

	internal fun id(path: String): Identifier = Identifier(MOD_ID, path)
}
