package com.example.wordcloudserverworker.repositories;


import com.example.wordcloudserverworker.models.TextFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TextFileRepository extends JpaRepository<TextFile, Long> {

}