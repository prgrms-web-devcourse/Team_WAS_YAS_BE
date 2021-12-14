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
	
	private final AmazonS3Client amazonS3Client;
	
	@Value(("${S3_BUCKET}") )
	private String bucket;
	
	public String upload(MultipartFile multipartFile, String dirName) throws IOException {
		File uploadFile = convert(multipartFile).orElseThrow(() -> new IllegalFileException(ErrorCode.INVALID_INPUT_ERROR));
		
		return upload(
				uploadFile,
				dirName
		);
	}
	
	private String upload(File uploadFile, String dirName) {
		String fileName = dirName + "/" + uploadFile.getName();
		String uploadImageUrl = putToS3(
				uploadFile,
				fileName
		);
		removeNewFile(uploadFile);
		
		return uploadImageUrl;
	}
	
	private String putToS3(File uploadFile, String fileName) {
		amazonS3Client.putObject(new PutObjectRequest(
				bucket,
				fileName,
				uploadFile
		).withCannedAcl(CannedAccessControlList.PublicRead));
		
		return amazonS3Client.getUrl(
				                     bucket,
				                     fileName
		                     )
		                     .toString();
	}
	
	private void removeNewFile(File targetFile) {
		if (!targetFile.delete()) {
			throw new IllegalFileException(ErrorCode.INVALID_INPUT_ERROR);
		}
	}
	
	private Optional<File> convert(MultipartFile file) throws IOException {
		File convertFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
		if (convertFile.createNewFile()) {
			try (FileOutputStream fos = new FileOutputStream(convertFile)) {
				fos.write(file.getBytes());
			}
			return Optional.of(convertFile);
		}
		return Optional.empty();
	}
}
