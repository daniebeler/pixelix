package com.daniebeler.pfpixelix.data.remote.dto

class UpdatePostDto(
    _status: String, _media_ids: List<String>?, _sensitive: Boolean?, _spoilerText: String?, _location: PlaceDto?
) {
    var status: String = _status
    var media_ids: List<String>? = _media_ids
    var sensitive: Boolean? = _sensitive
    var spoiler_text: String? = _spoilerText
    var location: PlaceDto? = _location
}