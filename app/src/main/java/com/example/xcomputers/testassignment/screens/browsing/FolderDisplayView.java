package com.example.xcomputers.testassignment.screens.browsing;

import com.pcloud.sdk.RemoteFolder;

import java.io.File;

/**
 * Created by Georgi on 4/19/2017.
 */

public interface FolderDisplayView {

    /**
     * Gives the view a folder to display on the screen
     * @param folder the folder from the cloud account to be displayed
     */
    void displayFolder(RemoteFolder folder);

    /**
     * Displays an error message
     * @param error the type of message to be displayed
     */
    void displayError(BrowsingPresenter.Error error);

    /**
     * Attempts to open a file if there is an app installed on the device that can handle the file type
     * @param file The file to be opened
     * @param contentType The content type of the file
     */
    void openFile(File file, String contentType);

    /**
     * Shows or hides a loading dialog on the screen
     * @param isLoading a boolean value to indicate if the dialog should be shown or hidden
     */
    void setLoadingState(boolean isLoading);
}
