package id.infiniteuny.apps.util

import java.net.URL

object RouteRequest {
    fun getKey(): String {
        val key: Array<String> = arrayOf(
            "b56d5554-79c3-4853-9d8d-ac36bb3b07cd",
            "55f3ef44-4c40-41a5-91ff-03cfbd330be3",
            "0c201b18-ff04-4a68-a63b-924045a4d20a",
            "e170f7aa-fa98-43cf-aa27-18170f7b4114",
            "9e3a4580-4a36-499f-8a22-ae0e0cf38b77",
            "cd68126c-a98c-423b-9866-670c8f4d1e9f",
            "2e06fa1b-a88a-4e99-8b54-129d27ed5148",
            "b129142a-aac6-4b20-95c9-b7cec32e9b6b",
            "96279f86-c1f5-4ee8-96a3-b319f5d2f450",
            "42cba860-e817-49a0-ab61-8c1d0069b107",
            "c716d8e9-0734-4c92-9074-054ebcdf921f",
            "2ad4ed1d-3b61-4a4f-8725-64ed87da7b18",
            "c2507117-2123-49bf-86c1-e32caf7f5455",
            "0c61182b-88e0-4761-8dfe-9d0ea3e4c02c",
            "0ba0c359-a5bf-43cf-bc2d-3409876c6539",
            "632c1eff-a257-4071-82dd-af1a44846d14",
            "8ccbaca0-9d26-464e-a6d8-60353f74a654",
            "da4f02e3-20b9-45a7-ad31-11bbc41f6a6b",
            "e2871407-17cf-4d64-beef-432d6ca89b2b",
            "9553763c-6d7a-49e2-9ad2-2746913fdee0",
            "6b781c1f-06d8-4b69-a431-eb28194badde",
            "2bf11687-c3a7-4203-945d-7f4df7e8cac1",
            "95dbebd7-54b4-4bed-8aee-2580dcc7afde",
            "f6860622-41b4-4e2f-9a5b-11a67f1f6274",
            "8e9bde6c-27b2-45ef-9d7a-3d216ab8a400",
            "e52d23fc-32c3-47a1-8a01-48b336ce1b8d",
            "d0c074f7-7a4a-4f41-ad9d-7e658804bcb8",
            "a4cf52d9-d946-4107-8399-0c198f07b758",
            "055d0f82-eedd-49d7-8b61-feb5c9fb5748",
            "1ce04c4b-3de1-4f00-81f7-72f224a7d652",
            "268739ad-ddfe-4c4a-82ec-6fda5316f9e6",
            "1bbd80f4-402f-45f9-bfff-317b128c0d0b",
            "4ff182d6-a563-4793-ba21-e47131d4b0fb",
            "57ca1446-866e-468c-ae47-0dcba350c050",
            "21e52a66-5414-464b-a72e-e12bbf715b8c",
            "39ed35ab-59f5-440c-ab93-19a3d12c0f67",
            "c99336c8-1f0a-411a-b02a-3953d1590b2c",
            "ab5e1b6a-fb34-4fa1-8ea6-de9f43cad230"
        )
        val rndm = (0..(key.size - 1)).random()
        logD("keyindex ${rndm}")
        return key[rndm]
    }

    fun requestRoute(latO: Double, lngO: Double, latD: Double, lngD: Double, directionBy: String): String {
//        val key = getKey()
        var url = "https://graphhopper.com/api/1/route?"
        url += "point=$latO,$lngO&point=$latD,$lngD"
        url += "&vehicle=$directionBy"
        url += "&key=841bb33f-36c9-4c5f-9fa0-4807928d9d58"
        url += "&type=json&points_encoded=false"
        return url
    }

    fun requestHttp(url: String): String {
        val res=URL(url).readText()
        logD("response $res")
        return res
    }
}