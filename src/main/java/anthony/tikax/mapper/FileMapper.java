package anthony.tikax.mapper;

import anthony.tikax.domain.model.UploadFileDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface FileMapper {

    @Insert("insert into file_upload (" +
            "file_md5, file_name, total_size, file_type, extension, mime_type, status, user_id, created_at) " +
            "values (#{fileMd5}, #{fileName}, #{totalSize}, #{fileType}, #{extension}, #{mimeType}, #{status}, #{userId}, #{createAt})")
    void insert(UploadFileDO uploadFileDO);

    @Select("select COUNT(*) from file_upload where file_md5 = #{md5}")
    boolean getFileByMd5(String md5);

    @Update("update file_upload set plain_text = #{plainText} where file_md5 = #{fileMd5}")
    void insertPlainText(String fileMd5, String plainText);

    @Update("delete from file_upload where user_id = #{userId} and file_name = #{fileName}")
    Boolean deleteFile(Integer userId, String fileName);
}
