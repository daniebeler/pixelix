package com.daniebeler.pfpixelix.data.remote.dto

class CreatePostDto(_status: String, _media_ids: List<String>, _sensitive: Boolean, _visibility: String, _spoilerText: String?, _place_id: String?) {
    var status: String = _status
    var media_ids: List<String> = _media_ids
    var sensitive: Boolean = _sensitive
    var spoiler_text: String? = _spoilerText
    var visibility: String? = _visibility
    var place_id: String? = _place_id
}