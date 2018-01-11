package com.promelle.amazon.dto;

import com.promelle.object.AbstractObject;

/**
 * This class handle amazon s3 upload object
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class AmazonUploadObjectAttribute extends AbstractObject {
	private static final long serialVersionUID = 2419719464587842210L;
	private String objectLocalPath;
	private String uploadFolderName;
	private String objectS3Path;
	private String objectExtension;
	private String fileName;

	public String getObjectLocalPath() {
		return objectLocalPath;
	}

	public AmazonUploadObjectAttribute setObjectLocalPath(String objectLocalPath) {
		this.objectLocalPath = objectLocalPath;
		return this;
	}

	public String getUploadFolderName() {
		return uploadFolderName;
	}

	public AmazonUploadObjectAttribute setUploadFolderName(
			String uploadFolderName) {
		this.uploadFolderName = uploadFolderName;
		return this;
	}

	public String getObjectS3Path() {
		return objectS3Path;
	}

	public AmazonUploadObjectAttribute setObjectS3Path(String objectS3Path) {
		this.objectS3Path = objectS3Path;
		return this;
	}

	public String getObjectExtension() {
		return objectExtension;
	}

	public AmazonUploadObjectAttribute setObjectExtension(String objectExtension) {
		this.objectExtension = objectExtension;
		return this;
	}

	public String getFileName() {
		return fileName;
	}

	public AmazonUploadObjectAttribute setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

}
