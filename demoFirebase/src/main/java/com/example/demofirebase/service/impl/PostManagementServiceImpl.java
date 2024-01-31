package com.example.demofirebase.service.impl;

import com.example.demofirebase.dto.PostDTO;
import com.example.demofirebase.firebase.FirebaseInitializer;
import com.example.demofirebase.service.PostManagementService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class PostManagementServiceImpl implements PostManagementService {

    @Autowired
    private FirebaseInitializer firebase;
    private Storage storage;

    @Override
    public List<PostDTO> list() {
        CollectionReference posts = firebase.getFirestore().collection("post");
        List<PostDTO> allPosts = new ArrayList<>();

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = posts.get();
        try {
            // Obtener el resultado de la consulta
            QuerySnapshot querySnapshot = querySnapshotApiFuture.get();
            // Iterar sobre los documentos
            for (QueryDocumentSnapshot document : querySnapshot) {
                PostDTO postDTO = document.toObject(PostDTO.class);
                postDTO.setId(document.getId());
                allPosts.add(postDTO);
            }
        } catch (InterruptedException | ExecutionException e) {
            // Manejar cualquier excepción que pueda ocurrir al obtener los documentos
            e.printStackTrace();
        }

        return allPosts;
    }

    @Override
    public Boolean add(PostDTO post) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("title", post.getTitle());
        docData.put("content", post.getContent());
        docData.put("tags", post.getTags());

        CollectionReference posts = firebase.getFirestore().collection("post");
        ApiFuture writeResultApiFuture = posts.document().create(docData);

        try{
            if(null != writeResultApiFuture.get()){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }catch (Exception e ){
            return Boolean.FALSE;
        }
    }

    @Override
    public Boolean edit(String id, PostDTO post) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("title", post.getTitle());
        docData.put("content", post.getContent());
        docData.put("tags", post.getTags());

        CollectionReference posts = firebase.getFirestore().collection("post");
        ApiFuture writeResultApiFuture = posts.document(id).update(docData);
        try{
            if(null != writeResultApiFuture.get()){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }catch (Exception e ){
            return Boolean.FALSE;
        }
    }

    @Override
    public Boolean delete(String id) {
        CollectionReference posts = firebase.getFirestore().collection("post");
        ApiFuture<WriteResult> deleteResultApiFuture = posts.document(id).delete();

        try{
            if(null != deleteResultApiFuture.get()){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }catch (Exception e ){
            return Boolean.FALSE;
        }
    }
}
