//todo kmp
//
//        runBlocking {
//            val loginData: LoginData? = currentLoginDataUseCase()
//            if (loginData == null || loginData.accessToken.isBlank() || loginData.baseUrl.isBlank()) {
//                val oldBaseurl: String? = repository.getAuthV1Baseurl().firstOrNull()
//                val oldAccessToken: String? = repository.getAuthV1Token().firstOrNull()
//                if (oldBaseurl != null && oldAccessToken != null && oldBaseurl.isNotBlank() && oldAccessToken.isNotBlank()) {
//                    repository.deleteAuthV1Data()
//                    updateAuthToV2(this@MainActivity, oldBaseurl, oldAccessToken)
//                } else {
//                    openLoginScreen()
//                }
//            } else {
//                if (loginData.accessToken.isNotEmpty()) {
//                    hostSelectionInterceptorInterface.setToken(loginData.accessToken)
//                }
//                if (loginData.baseUrl.isNotEmpty()) {
//                    hostSelectionInterceptorInterface.setHost(
//                        loginData.baseUrl.replace(
//                            "https://", ""
//                        )
//                    )
//                }
//                avatar = loginData.avatar
//            }
//        }
//
//        val imageUris = handleSharePhotoIntent(intent, contentResolver, cacheDir)
//
//
//                                LaunchedEffect(imageUris) {
//                                    imageUris.forEach { uri ->
//                                        try {
//                                            contentResolver.takePersistableUriPermission(
//                                                uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
//                                            )
//                                        } catch (e: SecurityException) {
//                                            e.printStackTrace() // Handle permission denial gracefully
//                                        }
//                                    }
//                                    if (imageUris.isNotEmpty()) {
//                                        val urisJson =
//                                            Json.encodeToString(imageUris.map { uri -> uri.toString() })
//                                        Navigate.navigate(
//                                            "new_post_screen?uris=$urisJson", navController
//                                        )
//                                    }
//                                }
//
//                                val destination = intent.extras?.getString(KEY_DESTINATION) ?: ""
//                                if (destination.isNotBlank()) {
//                                    // Delay the navigation action to ensure the graph is set
//                                    LaunchedEffect(Unit) {
//                                        when (destination) {
//                                            StartNavigation.Notifications.toString() -> Navigate.navigate(
//                                                "notifications_screen", navController
//                                            )
//
//                                            StartNavigation.Profile.toString() -> {
//                                                val accountId: String = intent.extras?.getString(
//                                                    KEY_DESTINATION_PARAM
//                                                ) ?: ""
//                                                if (accountId.isNotBlank()) {
//                                                    Navigate.navigate(
//                                                        "profile_screen/$accountId", navController
//                                                    )
//                                                }
//                                            }
//
//                                            StartNavigation.Post.toString() -> {
//                                                val postId: String = intent.extras?.getString(
//                                                    KEY_DESTINATION_PARAM
//                                                ) ?: ""
//                                                if (postId.isNotBlank()) {
//                                                    Navigate.navigate(
//                                                        "single_post_screen/$postId", navController
//                                                    )
//
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//
//
//fun saveUriToCache(uri: Uri, contentResolver: ContentResolver, cacheDir: File): Uri? {
//    try {
//        val inputStream: InputStream? = contentResolver.openInputStream(uri)
//        inputStream?.use { input ->
//            val file = File(cacheDir, "shared_image_${System.currentTimeMillis()}.jpg")
//            FileOutputStream(file).use { output ->
//                input.copyTo(output)
//            }
//            return Uri.fromFile(file) // Return the new cached URI
//        }
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//    return null
//}
//
//private fun handleSharePhotoIntent(
//    intent: Intent, contentResolver: ContentResolver, cacheDir: File
//): List<Uri> {
//    val action = intent.action
//    val type = intent.type
//    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//
//    var imageUris: List<Uri> = emptyList()
//    when {
//        Intent.ACTION_SEND == action && type != null -> {
//            if (type.startsWith("image/") || type.startsWith("video/")) {
//                val singleUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                    intent.getParcelableExtra(
//                        Intent.EXTRA_STREAM, Uri::class.java
//                    )
//                } else {
//                    @Suppress("DEPRECATION") intent.getParcelableExtra(
//                        Intent.EXTRA_STREAM
//                    ) as? Uri
//                }
//                singleUri?.let { uri ->
//                    val cachedUri = saveUriToCache(uri, contentResolver, cacheDir)
//                    imageUris =
//                        cachedUri?.let { listOf(it) } ?: emptyList() // Wrap single image in a list
//                }
//            }
//        }
//
//        Intent.ACTION_SEND_MULTIPLE == action && type != null -> {
//            val receivedUris = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                intent.getParcelableArrayListExtra(
//                    Intent.EXTRA_STREAM, Uri::class.java
//                )
//            } else {
//                @Suppress("DEPRECATION") intent.getParcelableArrayListExtra(
//                    Intent.EXTRA_STREAM
//                )
//            }
//            imageUris = receivedUris?.mapNotNull {
//                saveUriToCache(
//                    it, contentResolver, cacheDir
//                )
//            } ?: emptyList()
//        }
//    }
//    return imageUris
//}
//
//fun updateAuthToV2(context: Context, baseUrl: String, accessToken: String) {
//    val intent = Intent(context, LoginActivity::class.java)
//    intent.putExtra("base_url", baseUrl)
//    intent.putExtra("access_token", accessToken)
//    context.startActivity(intent)
//}
