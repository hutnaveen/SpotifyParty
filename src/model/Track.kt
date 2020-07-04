package model

import java.awt.Color
import java.net.URL


data class Track @JvmOverloads constructor(var id: String = "",
                                           var name: String = "",
                                           var artist: String = "",
                                           var thumbnailURL: URL? = null,
                                           var audioPreview: URL? = null,
                                           var dominantColor: Color?= null,
                                           var popularity: Int? = null)

