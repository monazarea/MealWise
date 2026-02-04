package com.example.mealwise.utils;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.credentials.CredentialManager;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import java.util.Collections;
import io.reactivex.rxjava3.core.Single;

public class GoogleAuthManager {

    private final Context context;
    private final CredentialManager credentialManager;
    private final String webClientId;

    public GoogleAuthManager(Context context, String webClientId) {
        this.context = context;
        this.webClientId = webClientId;
        this.credentialManager = CredentialManager.create(context);
    }

    public Single<String> signIn() {
        return Single.create(emitter -> {

            GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(webClientId)
                    .setAutoSelectEnabled(false)
                    .build();

            GetCredentialRequest request = new GetCredentialRequest.Builder()
                    .setCredentialOptions(Collections.singletonList(googleIdOption))
                    .build();

            credentialManager.getCredentialAsync(
                    context,
                    request,
                    new android.os.CancellationSignal(),
                    ContextCompat.getMainExecutor(context),
                    new androidx.credentials.CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                        @Override
                        public void onResult(GetCredentialResponse result) {
                            try {
                                CustomCredential credential = (CustomCredential) result.getCredential();
                                if (credential.getType().equals(GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {
                                    GoogleIdTokenCredential tokenCredential = GoogleIdTokenCredential.createFrom(credential.getData());

                                    if (!emitter.isDisposed()) {
                                        emitter.onSuccess(tokenCredential.getIdToken());
                                    }
                                } else {
                                    if (!emitter.isDisposed()) {
                                        emitter.onError(new Throwable("Unknown Credential Type"));
                                    }
                                }
                            } catch (Exception e) {
                                if (!emitter.isDisposed()) {
                                    emitter.onError(e);
                                }
                            }
                        }

                        @Override
                        public void onError(GetCredentialException e) {
                            if (!emitter.isDisposed()) {
                                emitter.onError(e);
                            }
                        }
                    }
            );
        });
    }
}