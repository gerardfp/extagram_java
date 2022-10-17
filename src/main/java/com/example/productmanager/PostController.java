package com.example.productmanager;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class PostController {
    @Autowired private PostRepository postRepository;
    @Autowired private MinioClient minioClient;
    @Value("${minio.bucket.name}")  private String bucketName;


    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("posts", postRepository.findAll());
        return "index";
    }

    @PostMapping("/save")
    public String save(@RequestParam("photo") MultipartFile uploadedFile, @RequestParam("name") String name) throws Exception {
            String uuid = UUID.randomUUID().toString();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(uuid)
                    .stream(uploadedFile.getInputStream(), uploadedFile.getSize(), -1)
                    .build());

            postRepository.save(new Post(name, uuid));
            return "redirect:/";
    }

    @GetMapping("/photo/{id}")
    public ResponseEntity<?> getPhoto(@PathVariable String id) throws Exception {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(IOUtils.toByteArray(
                            minioClient.getObject(GetObjectArgs.builder()
                                    .bucket(bucketName)
                                    .object(id)
                                    .build())));
    }

    @GetMapping(path = "/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "redirect:/";
    }

    @GetMapping(path = "/error")
    public String error(){
        return "error";
    }
}
