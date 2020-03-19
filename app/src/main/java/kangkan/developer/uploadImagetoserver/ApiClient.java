package kangkan.developer.uploadImagetoserver;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL="https://untearable-trays.000webhostapp.com/ImageUploadApi/";
    private static Retrofit retrofit;

    public static Retrofit getApiClient()
    {
        if (retrofit==null){
            retrofit=new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
