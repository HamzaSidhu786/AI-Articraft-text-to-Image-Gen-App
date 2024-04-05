package com.hamza.aiarticraft.ui.api

data class ApiResponse(
    val image: String,
    val seed: Long,
    val cost: Double
)


data class Parameters(
    val prompt: String,
    val negative_prompt: String,
    val styles: List<String>?,
    val seed: Long,
    val subseed: Long,
    val subseed_strength: Int,
    val seed_resize_from_h: Int,
    val seed_resize_from_w: Int,
    val sampler_name: String?,
    val batch_size: Int,
    val n_iter: Int,
    val steps: Int,
    val cfg_scale: Float,
    val width: Int,
    val height: Int,
    val restore_faces: Boolean?,
    // Add other properties as needed
    val sampler_index: String,
    val script_name: String?,
    val script_args: List<String>,
    val send_images: Boolean,
    val save_images: Boolean,
    val alwayson_scripts: Map<String, Any>
)

