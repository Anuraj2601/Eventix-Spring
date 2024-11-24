package com.example.eventix.service;

import com.example.eventix.dto.EventPostDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.entity.EventPost;
import com.example.eventix.entity.Users;
import com.example.eventix.repository.EventPostRepo;
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

import static com.example.eventix.constant.Constant.EVENT_POST_PHOTO_DIRECTORY;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Transactional
public class EventPostService {

    @Autowired
    private EventPostRepo eventPostRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResponseDTO responseDTO;

    @Autowired
    private UsersRepo usersRepo;

    public ResponseDTO saveEventPost(EventPostDTO eventPostDTO, MultipartFile file) {
        try{
            if(eventPostRepo.existsById(eventPostDTO.getE_post_id())){
                responseDTO.setStatusCode(VarList.RSP_DUPLICATED);
                responseDTO.setMessage("Event Post already exists");
                responseDTO.setContent(eventPostDTO);

            }else{
                eventPostDTO.setPost_image(photoFunction.apply(0,file));


                //Manually setting up the published user id
                EventPost eventPost = modelMapper.map(eventPostDTO, EventPost.class);
                Users user = usersRepo.findById(eventPostDTO.getPublished_user_id())
                        .orElseThrow(() -> new RuntimeException("User not found"));
                eventPost.setPublished_user(user);


                EventPost savedEventPost = eventPostRepo.save(eventPost);
                EventPostDTO savedEventPostDTO = modelMapper.map(savedEventPost, EventPostDTO.class);
                savedEventPostDTO.setPublished_user_id(user.getId());
                //System.out.println("Published user id in post service saved post after setting publisher id manually"+ savedPostDTO);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Post Saved Successfully");
                responseDTO.setContent(savedEventPostDTO);
            }

            return responseDTO;

        }catch(Exception e){

            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;

        }
    }

    public ResponseDTO getAllEventPosts(){
        try{
            List<EventPost> eventPostsList = eventPostRepo.findAll();
            if(!eventPostsList.isEmpty()){
                List<EventPostDTO> eventPostDTOList = modelMapper.map(eventPostsList, new TypeToken<List<EventPostDTO>>(){}.getType());

                // Manually set the published_user_id for each PostDTO
                for (int i = 0; i < eventPostsList.size(); i++) {
                    eventPostDTOList.get(i).setPublished_user_id(eventPostsList.get(i).getPublished_user().getId());
                }

                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Retrieved All Event Posts Successfully");
                responseDTO.setContent(eventPostDTOList);

            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event Posts Found");
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

    public ResponseDTO getEventPost(int eventPostId){
        try{
            if(eventPostRepo.existsById(eventPostId)){
                EventPost eventPost = eventPostRepo.findById(eventPostId).orElse(null);
                EventPostDTO eventPostDTO =  modelMapper.map(eventPost, EventPostDTO.class);
                //postDTO.setPublished_user_id(post.getPublished_user().getId());

                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Post Retrieved Successfully");
                responseDTO.setContent(eventPostDTO);

            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event Post Found");
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

    public ResponseDTO updateEventPost(int eventPostId, EventPostDTO eventPostDTO, MultipartFile file){
        try{
            if(eventPostRepo.existsById(eventPostId)){

                // Fetch the existing post from the database
                EventPost existingEventPost = eventPostRepo.findById(eventPostId).orElseThrow(() -> new RuntimeException("Event Post not found"));

                // If a new image is uploaded, update the image, otherwise keep the existing image
                if (file != null && !file.isEmpty()) {
                    eventPostDTO.setPost_image(photoFunction.apply(0, file));
                } else {
                    eventPostDTO.setPost_image(existingEventPost.getPost_image());
                }


                //Manually setting up the published user id
//                Post post = modelMapper.map(postDTO, Post.class);
//                Users user = usersRepo.findById(postDTO.getPublished_user_id())
//                        .orElseThrow(() -> new RuntimeException("User not found"));
//                post.setPublished_user(user);



                EventPost updatedEventPost =  eventPostRepo.save(modelMapper.map(eventPostDTO, EventPost.class));
                EventPostDTO updatedEventPostDTO = modelMapper.map(updatedEventPost, EventPostDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Post Updated Successfully");
                responseDTO.setContent(updatedEventPostDTO);


            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event Post Found");
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

    public ResponseDTO deleteEventPost(int eventPostId){
        try{
            if(eventPostRepo.existsById(eventPostId)){

                //Deleting image when deleting post
                EventPost eventPost = eventPostRepo.findById(eventPostId).orElse(null);
                if(eventPost != null && eventPost.getPost_image() != null){

                    String imagePath = eventPost.getPost_image();

                    String fileName = imagePath.substring(imagePath.lastIndexOf("/") + 1);

                    Path imageFilePath = Paths.get(EVENT_POST_PHOTO_DIRECTORY).resolve(fileName);

                    try{
                        Files.deleteIfExists(imageFilePath);
                        System.out.println("Image deleted successfully"+ imageFilePath);

                    }catch(IOException e){
                        System.out.println("Error deleting image"+ e.getMessage());
                    }

                }

                eventPostRepo.deleteById(eventPostId);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Post Deleted Successfully");
                responseDTO.setContent(null);


            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event Post Found");
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
            Path fileStorageLocation = Paths.get(EVENT_POST_PHOTO_DIRECTORY).toAbsolutePath().normalize();
            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }
            Files.copy(image.getInputStream(),fileStorageLocation.resolve(randomFileName), REPLACE_EXISTING);
            return ServletUriComponentsBuilder.fromCurrentContextPath().path("/uploads/events/eventPosts/" + randomFileName).toUriString();
        } catch (Exception exception) {
            throw new RuntimeException("Unable to save image");
        }
    };


}
