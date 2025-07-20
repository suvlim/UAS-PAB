package com.uph23.edu.sahabattani;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;

public class DialogKonfirmasi extends Dialog {
    private final Context context;
    private final OnConfirmListener onConfirmListener;

    public interface OnConfirmListener {
        void onConfirm();
    }

    public DialogKonfirmasi(@NonNull Context context, OnConfirmListener listener) {
        super(context);
        this.context = context;
        this.onConfirmListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // Hilangkan judul default
        setContentView(R.layout.popup_confirmation);

        // Inisialisasi tombol
        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnConfirm = findViewById(R.id.btnConfirm);

        // Aksi tombol Batal
        btnCancel.setOnClickListener(v -> dismiss());

        // Aksi tombol Ya
        btnConfirm.setOnClickListener(v -> {
            if (onConfirmListener != null) {
                onConfirmListener.onConfirm();
            }
            dismiss();
        });

        // Atur posisi dialog di tengah
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent); // Efek border melengkung
            window.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
