package com.example.wordcloudserverworker.models;

import com.example.wordcloudserverworker.enums.WordCloudStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "text_file")
public class TextFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    private String name;
    private Long size;
    private WordCloudStatus status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "words_id", referencedColumnName = "id")
    private Words words;
}
