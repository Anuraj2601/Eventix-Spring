package com.example.eventix.service;


import com.example.eventix.constant.Constant;
import com.example.eventix.dto.PostDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.entity.Post;
import com.example.eventix.entity.Users;
import com.example.eventix.repository.PostRepo;
import com.example.eventix.repository.UsersRepo;
import com.example.eventix.util.VarList;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.example.eventix.constant.Constant.PHOTO_DIRECTORY;
import static com.example.eventix.constant.Constant.POST_PHOTO_DIRECTORY;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Transactional
public class PostService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResponseDTO responseDTO;

    public ResponseDTO savePost(PostDTO postDTO, MultipartFile file) {
        try{
            if(postRepo.existsById(postDTO.getPost_id())){
                responseDTO.setStatusCode(VarList.RSP_DUPLICATED);
                responseDTO.setMessage("Post already exists");
                responseDTO.setContent(postDTO);

            }else{
                postDTO.setPost_image(photoFunction.apply(0,file));
                //System.out.println("Published user id in post service before save"+ postDTO);

                //Manually setting up the published user id
                Post post = modelMapper.map(postDTO, Post.class);
                Users user = usersRepo.findById(postDTO.getPublished_user_id())
                        .orElseThrow(() -> new RuntimeException("User not found"));
                post.setPublished_user(user);

                //System.out.println("Published user id in post service after mapping" + post);
                //Post savedPost = postRepo.save(modelMapper.map(postDTO, Post.class));
                Post savedPost = postRepo.save(post);
                //System.out.println("Published user id in post service saved post"+ savedPost);
                PostDTO savedPostDTO = modelMapper.map(savedPost, PostDTO.class);
                //System.out.println("Published user id in post service saved post DTO"+ savedPostDTO);
                savedPostDTO.setPublished_user_id(user.getId());
                //System.out.println("Published user id in post service saved post after setting publisher id manually"+ savedPostDTO);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Post Saved Successfully");
                responseDTO.setContent(savedPostDTO);
            }

            return responseDTO;

        }catch(Exception e){

            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;

        }
    }

    public ResponseDTO getAllPosts(){
        try{
            List<Post> postsList = postRepo.findAll();
            if(!postsList.isEmpty()){
                List<PostDTO> postDTOList = modelMapper.map(postsList, new TypeToken<List<PostDTO>>(){}.getType());

                // Manually set the published_user_id for each PostDTO
                for (int i = 0; i < postsList.size(); i++) {
                    postDTOList.get(i).setPublished_user_id(postsList.get(i).getPublished_user().getId());
                }

                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Retrieved All Posts Successfully");
                responseDTO.setContent(postDTOList);

            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Posts Found");
                responseDTO.setContent(null);

            }

            return responseDTO;


        }catch(Exception e){

            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;

        }
    }

    public ResponseDTO getPost(int post_id){
        try{
            if(postRepo.existsById(post_id)){
                Post post = postRepo.findById(post_id).orElse(null);
                PostDTO postDTO =  modelMapper.map(post, PostDTO.class);
                //postDTO.setPublished_user_id(post.getPublished_user().getId());

                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Post Retrieved Successfully");
                responseDTO.setContent(postDTO);

            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Post Found");
                responseDTO.setContent(null);


            }

            return responseDTO;

        }catch(Exception e){
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;

        }
    }

    public ResponseDTO updatePost(int post_id, PostDTO postDTO, MultipartFile file){
        try{
            if(postRepo.existsById(post_id)){

                // Fetch the existing post from the database
                Post existingPost = postRepo.findById(post_id).orElseThrow(() -> new RuntimeException("Post not found"));

                // If a new image is uploaded, update the image, otherwise keep the existing image
                if (file != null && !file.isEmpty()) {
                    postDTO.setPost_image(photoFunction.apply(0, file));
                } else {
                    postDTO.setPost_image(existingPost.getPost_image());
                }


                //Manually setting up the published user id
//                Post post = modelMapper.map(postDTO, Post.class);
//                Users user = usersRepo.findById(postDTO.getPublished_user_id())
//                        .orElseThrow(() -> new RuntimeException("User not found"));
//                post.setPublished_user(user);


                //postDTO.setPost_image(photoFunction.apply(0,file));
                Post updatedPost =  postRepo.save(modelMapper.map(postDTO, Post.class));
                //Post updatedPost =  postRepo.save(post);
                PostDTO updatedPostDTO = modelMapper.map(updatedPost, PostDTO.class);
                //updatedPostDTO.setPublished_user_id(user.getId());
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Post Updated Successfully");
                responseDTO.setContent(updatedPostDTO);


            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Post Found");
                responseDTO.setContent(null);

            }

            return responseDTO;

        }catch(Exception e){
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;

        }
    }

    public ResponseDTO deletePost(int post_id){
        try{
            if(postRepo.existsById(post_id)){

                //Deleting image when deleting post
                Post post = postRepo.findById(post_id).orElse(null);
                if(post != null && post.getPost_image() != null){

                    String imagePath = post.getPost_image();

                    String fileName = imagePath.substring(imagePath.lastIndexOf("/") + 1);

                    Path imageFilePath = Paths.get(POST_PHOTO_DIRECTORY).resolve(fileName);

                    try{
                        Files.deleteIfExists(imageFilePath);
                        System.out.println("Image deleted successfully"+ imageFilePath);

                    }catch(IOException e){
                        System.out.println("Error deleting image"+ e.getMessage());
                    }

                }

                postRepo.deleteById(post_id);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Post Deleted Successfully");
                responseDTO.setContent(null);


            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Post Found");
                responseDTO.setContent(null);

            }

            return responseDTO;

        }catch(Exception e){
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(e);
            return responseDTO;
        }
    }

    public ResponseDTO updatePostStatus(int post_id, String status){

        try{
            if(postRepo.existsById(post_id)){
                Post existingPost = postRepo.findById(post_id).orElseThrow(() -> new RuntimeException("Post not found"));

                //existingPost.setPost_status(status);
                Post.postType postStatusEnum = Post.postType.valueOf(status.toUpperCase());
                existingPost.setPost_status(postStatusEnum);  // Set the post status

                postRepo.save(existingPost);  // Save the updated post

                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Post status updated successfully");
                responseDTO.setContent(existingPost);

            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Post Found");
                responseDTO.setContent(null);
            }

            return responseDTO;

        }catch(Exception e){
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(e);
            return responseDTO;

        }

    }

    private final Function<String, String> fileExtension = filename -> Optional.of(filename).filter(name -> name.contains("."))
            .map(name -> "." + name.substring(filename.lastIndexOf(".") + 1)).orElse(".png");

    private final BiFunction<Integer,MultipartFile,String> photoFunction = (id, image) -> {
        String originalFilename = image.getOriginalFilename();
        String fileExtension1 = fileExtension.apply(originalFilename);

        String randomFileName = UUID.randomUUID().toString() + fileExtension1;
        try {
            Path fileStorageLocation = Paths.get(POST_PHOTO_DIRECTORY).toAbsolutePath().normalize();
            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }
            Files.copy(image.getInputStream(),fileStorageLocation.resolve(randomFileName), REPLACE_EXISTING);
            return ServletUriComponentsBuilder.fromCurrentContextPath().path("/uploads/posts/" + randomFileName).toUriString();
        } catch (Exception exception) {
            throw new RuntimeException("Unable to save image");
        }
    };

}
