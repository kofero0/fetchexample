package mdrew.jsonexample

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform