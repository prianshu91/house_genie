package com.promelle.amazon.upload;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.promelle.amazon.dto.AmazonUploadObjectAttribute;
import com.promelle.exception.AbstractException;
import com.promelle.map.MaxSizeHashMap;
import com.promelle.utils.FileUtils;

/**
 * This class is intended for providing s3 related functions.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class S3Uploader {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(S3Uploader.class.getName());
	public static final String FILE_PREFIX = "https://s3.amazonaws.com/";
	public static final String BUCKET_NAME = "info-common";
	private static final String ACCESS_KEY_ID = "AKIAJ6BWSZW7UJW2HYAA";
	private static final String SECRET_ACCESS_KEY = "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";
	public static final String SUFFIX = "/";
	private AWSCredentials credentials;
	private AmazonS3 s3client;
	private MaxSizeHashMap<String, Object> buffer;

	public S3Uploader() {
		if (StringUtils.isNotBlank(ACCESS_KEY_ID)
				&& StringUtils.isNotBlank(SECRET_ACCESS_KEY)) {
			credentials = new BasicAWSCredentials(ACCESS_KEY_ID,
					SECRET_ACCESS_KEY);
			s3client = new AmazonS3Client(credentials);
			// create bucket - name must be unique for all S3 users
			// s3client.createBucket(BUCKET_NAME);
		}
		buffer = new MaxSizeHashMap<String, Object>(10);
	}

	public String upload(AmazonUploadObjectAttribute objectAttribute,
			Boolean isNewFolderCreated) throws AbstractException {
		String targetFolderName = StringUtils.isEmpty(objectAttribute
				.getUploadFolderName()) ? "assets" : objectAttribute
				.getUploadFolderName();
		// create folder into bucket
		if (isNewFolderCreated) {
			createFolder(BUCKET_NAME, targetFolderName);
		}
		// upload file to folder and set it to public
		String bucketFileName = targetFolderName
				+ SUFFIX
				+ (StringUtils.isBlank(objectAttribute.getFileName()) ? new Date()
						.getTime() : objectAttribute.getFileName())
				+ objectAttribute.getObjectExtension();
		s3client.putObject(new PutObjectRequest(BUCKET_NAME, bucketFileName,
				new File(objectAttribute.getObjectLocalPath()))
				.withCannedAcl(CannedAccessControlList.PublicRead));
		return FILE_PREFIX + BUCKET_NAME + SUFFIX + bucketFileName;
	}

	public String upload(InputStream inputStream, String folderName,
			String fileName, String extension, Boolean isNewFolderCreated)
			throws IOException, AbstractException {
		return upload(FileUtils.writeToTempFile(inputStream, extension),
				folderName, fileName, extension, isNewFolderCreated);
	}

	public String upload(File file, String folderName, String fileName,
			String extension, Boolean isNewFolderCreated) throws IOException,
			AbstractException {
		AmazonUploadObjectAttribute uploadObjectAttribute = new AmazonUploadObjectAttribute();
		uploadObjectAttribute.setFileName(fileName);
		uploadObjectAttribute.setObjectExtension(extension);
		uploadObjectAttribute.setObjectLocalPath(file.getAbsolutePath());
		uploadObjectAttribute.setObjectS3Path(null);
		uploadObjectAttribute.setUploadFolderName(folderName);
		return upload(uploadObjectAttribute, isNewFolderCreated);
	}

	public void createFolder(String bucketName, String folderName)
			throws AbstractException {
		if (StringUtils.isBlank(folderName)) {
			throw new AbstractException("folder.name.required")
					.setCustomMessage("Folder name is required");
		}
		if (buffer.containsKey(folderName)) {
			return;
		}
		// if (s3client.doesBucketExist(bucketName)) {
		// s3client.createBucket(bucketName);
		// }
		LOGGER.info("CREATE FOLDER");
		// create meta-data for your folder and set content-length to 0
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);
		// create empty content
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
		// create a PutObjectRequest passing the folder name suffixed by /
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
				folderName + SUFFIX, emptyContent, metadata);
		// send request to S3 to create folder
		s3client.putObject(putObjectRequest);
		buffer.put(folderName, 1);
	}
	
	public String createFile(String bucketName, String fileName, 
			String extension, String content)
			throws AbstractException, IOException {
		if (StringUtils.isBlank(fileName)) {
			throw new AbstractException("file.name.required")
					.setCustomMessage("File name is required");
		}
		String targetFolderName = "documents";
		createFolder(BUCKET_NAME, targetFolderName);
		
		String bucketFileName = targetFolderName
				+ SUFFIX + fileName + extension;

		File docFileToUpload = null;
		try {
			docFileToUpload = FileUtils.createFileAndWriteContent(bucketFileName, 
					extension, content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return upload(docFileToUpload, targetFolderName, 
				fileName, extension, true);
	}

	public S3Object downloadObject(String bucketName, String imageId) 
			throws IOException {
		if(s3client.doesBucketExist(BUCKET_NAME + SUFFIX + bucketName)) {//S3Object s3Object = s3client.doesBucketExist(bucketName);
			GetObjectRequest getObjectRequest = new GetObjectRequest(BUCKET_NAME + SUFFIX + bucketName, imageId);
			return s3client.getObject(getObjectRequest);
		} 
		return null;
	}	
}
