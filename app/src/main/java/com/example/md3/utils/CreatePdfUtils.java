package com.example.md3.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Keep;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;

@Keep
public class CreatePdfUtils {

    public static File writePdfOBJToDisk(ResponseBody body, Context context,String id,String pdfName) {
        try
        {
            File gstInvoiceMakerDir=null;



                File parentFIle;
                if(context.getExternalMediaDirs().length>0){
                    parentFIle = context.getExternalMediaDirs()[0];
                }else{
                    parentFIle = context.getFilesDir();
                }


                if (id.equals("image")) {
                    gstInvoiceMakerDir = new File(parentFIle, "GSTInvoiceFestiveGreetings");
                } else if (id.equals("csv")) {
                    gstInvoiceMakerDir = new File(parentFIle, "GSTInvoiceXLSFiles");
                } else if (id.equals("zip")) {
                    gstInvoiceMakerDir = new File(parentFIle, "GSTInvoiceBulkReport");
                } else {
                    gstInvoiceMakerDir = new File(parentFIle, "GSTInvoiceMaker");
                }

            File futureStudioIconFile;

            if (!gstInvoiceMakerDir.exists()) {
                gstInvoiceMakerDir.mkdir();
            }

            Pattern regex = Pattern.compile("[/]");
            Matcher matcher = regex.matcher(id);
            if (matcher.find()) {
//                id = id.replaceAll("\\/", "_");
                id = id.replaceAll("[^a-zA-Z0-9]", "_");
            }
            if (id.equals("image")) {
                futureStudioIconFile = new File(gstInvoiceMakerDir + File.separator + pdfName + ".png");
            } else if (id.equals("csv")) {
                futureStudioIconFile = new File(gstInvoiceMakerDir + File.separator + pdfName + ".csv");
            } else if (id.equals("zip")) {
                futureStudioIconFile = new File(gstInvoiceMakerDir + File.separator + pdfName + ".zip");
            } else {
                futureStudioIconFile = new File(gstInvoiceMakerDir + File.separator + pdfName + "_" + id + ".pdf");
            }

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {

                //returns false if no space available in device

                if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
                    ViewUtils.showToast(context,"Space not available.", Toast.LENGTH_SHORT);
                }
                else {

                    byte[] fileReader = new byte[4096];

                    long fileSize = body.contentLength();
                    long fileSizeDownloaded = 0;

                    inputStream = body.byteStream();
                    outputStream = new FileOutputStream(futureStudioIconFile);

                    while (true) {
                        int read = inputStream.read(fileReader);

                        if (read == -1) {
                            break;
                        }

                        outputStream.write(fileReader, 0, read);

                        fileSizeDownloaded += read;

                    }

                    outputStream.flush();
                    try {
                        if (futureStudioIconFile.isFile()) {
                            if (futureStudioIconFile.exists()) {
                                Log.d("exist", "exist" + futureStudioIconFile.getName());
                                Log.d("exist", "exist" + futureStudioIconFile.getAbsolutePath());
                                Log.d("exist", "exist" + futureStudioIconFile.getParentFile());
                                Log.d("exist", "exist" + futureStudioIconFile.getCanonicalPath());
                                Log.d("exist", "exist" + futureStudioIconFile.getPath());
                            }
                        } else {
                            Log.d("noexist", "fileis exist------" + futureStudioIconFile.exists());

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }


                return futureStudioIconFile;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }

        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static File writePdfOBJToTempFile(ResponseBody body, Context context,String fileName) {
        try
        {
//            File gstInvoiceMakerDir= new File(fileName);
            File gstInvoiceMakerDir= new File(context.getCacheDir(), fileName);


//
//
//
//            File parentFIle;
////            if(context.getExternalMediaDirs().length>0){
////                parentFIle = context.getExternalMediaDirs()[0];
////            }else{
////                parentFIle = context.getFilesDir();
////            }
//
//
//            if (id.equals("image")) {
//                gstInvoiceMakerDir = new File(parentFIle, "GSTInvoiceFestiveGreetings");
//            } else if (id.equals("csv")) {
//                gstInvoiceMakerDir = new File(parentFIle, "GSTInvoiceXLSFiles");
//            } else if (id.equals("zip")) {
//                gstInvoiceMakerDir = new File(parentFIle, "GSTInvoiceBulkReport");
//            } else {
//                gstInvoiceMakerDir = new File(parentFIle, "GSTInvoiceMaker");
//            }
//
//            File futureStudioIconFile;
//
//            if (!gstInvoiceMakerDir.exists()) {
//                gstInvoiceMakerDir.mkdir();
//            }
//
//            Pattern regex = Pattern.compile("[/]");
//            Matcher matcher = regex.matcher(id);
//            if (matcher.find()) {
////                id = id.replaceAll("\\/", "_");
//                id = id.replaceAll("[^a-zA-Z0-9]", "_");
//            }
//            if (id.equals("image")) {
//                futureStudioIconFile = new File(gstInvoiceMakerDir + File.separator + pdfName + ".png");
//            } else if (id.equals("csv")) {
//                futureStudioIconFile = new File(gstInvoiceMakerDir + File.separator + pdfName + ".csv");
//            } else if (id.equals("zip")) {
//                futureStudioIconFile = new File(gstInvoiceMakerDir + File.separator + pdfName + ".zip");
//            } else {
//                futureStudioIconFile = new File(gstInvoiceMakerDir + File.separator + pdfName + "_" + id + ".pdf");
//            }
//
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
//
//                //returns false if no space available in device
//
//                if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
//                    ViewUtils.showAlertDialog(context,"Alert!", "Space not available.");
//                }
//                else {
//
                    byte[] fileReader = new byte[4096];

//                    long fileSize = body.contentLength();
                    long fileSizeDownloaded = 0;

                    inputStream = body.byteStream();
                    outputStream = new FileOutputStream(gstInvoiceMakerDir);

                    while (true) {
                        int read = inputStream.read(fileReader);

                        if (read == -1) {
                            break;
                        }

                        outputStream.write(fileReader, 0, read);

                        fileSizeDownloaded += read;

                    }
//
                    outputStream.flush();
//                    try {
//                        if (futureStudioIconFile.isFile()) {
//                            if (futureStudioIconFile.exists()) {
                                Log.d("FileIssueChecked", "--------exist---------" + gstInvoiceMakerDir.toString());

//                            }
//                        } else {
//                            Log.d("noexist", "fileis exist------" + futureStudioIconFile.exists());
//
//                        }
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//
//
                return gstInvoiceMakerDir;
            } catch (IOException e) {
                e.printStackTrace();

                Log.d("FileIssueChecked", "writePdfOBJToTempFile: "+e.getMessage());
                return null;
            }
            finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
