package com.example.eventix.service;


import com.example.eventix.dto.PostDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.entity.Post;
import com.example.eventix.repository.PostRepo;
import com.example.eventix.util.VarList;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PostService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResponseDTO responseDTO;

    public ResponseDTO savePost(PostDTO postDTO) {
        try{
            if(postRepo.existsById(postDTO.getPost_id())){
                responseDTO.setStatusCode(VarList.RSP_DUPLICATED);
                responseDTO.setMessage("Post already exists");
                responseDTO.setContent(postDTO);

            }else{
                Post savedPost = postRepo.save(modelMapper.map(postDTO, Post.class));
                PostDTO savedPostDTO = modelMapper.map(savedPost, PostDTO.class);
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

    public ResponseDTO updatePost(int post_id, PostDTO postDTO){
        try{
            if(postRepo.existsById(post_id)){
                Post updatedPost =  postRepo.save(modelMapper.map(postDTO, Post.class));
                PostDTO updatedPostDTO = modelMapper.map(updatedPost, PostDTO.class);
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

}
