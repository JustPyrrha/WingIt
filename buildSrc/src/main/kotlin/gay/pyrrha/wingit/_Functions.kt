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

import org.gradle.api.Project
import java.util.*

fun Project.isModStable(): Boolean =
    isModAlpha().not() && isModBeta().not()

fun Project.isModAlpha(): Boolean =
    project.version.toString().contains("-a") || project.version.toString().contains("-alpha")

fun Project.isModBeta(): Boolean =
    project.version.toString().contains("-b") || project.version.toString().contains("-beta")


fun String?.base64Decode(): String? =
    if (this != null) String(Base64.getDecoder().decode(this)) else null
