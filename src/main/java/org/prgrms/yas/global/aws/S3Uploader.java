package org.prgrms.yas.global.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.user.exception.IllegalFileException;
import org.prgrms.yas.global.error.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class S3Uploader {
	
	private final AmazonS3Client s3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	public String upload(MultipartFile file, String dirName) throws IOException {
		InputStream uploadFile = file.getInputStream();
		String fileName = dirName + "/" + uploadFile;

		ObjectMetadata objectMetadata = new ObjectMetadata();
		byte[] bytes = IOUtils.toByteArray(file.getInputStream());
		objectMetadata.setContentLength(bytes.length);

		s3Client.putObject(
			new PutObjectRequest(bucket, fileName, file.getInputStream(), objectMetadata)
				.withCannedAcl(CannedAccessControlList.PublicRead)
		);

		return s3Client
			.getUrl(bucket, fileName)
			.toString();
	}
}
