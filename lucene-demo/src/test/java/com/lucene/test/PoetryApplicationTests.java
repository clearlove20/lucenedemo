package com.lucene.test;
import com.lucene.dao.PoetryDAO;
import com.lucene.pojo.Poetry;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PoetryApplicationTests {

	@Autowired
	private PoetryDAO poetryDAO;
	@Test
	public void contextLoads() throws IOException {

        List<Poetry> potries = poetryDAO.findAll();

        System.out.println(potries.size());

        FSDirectory fsDirectory = FSDirectory.open(Paths.get("F://index/demo5"));

        IndexWriter indexWriter = new IndexWriter(fsDirectory, new IndexWriterConfig(new StandardAnalyzer()));

        for (Poetry potry : potries) {
            Document document = new Document();
            document.add(new IntField("id",potry.getId(), Field.Store.YES));
            document.add(new StringField("title",potry.getTitle(), Field.Store.YES));
            document.add(new TextField("content",potry.getContent(),Field.Store.YES));
            document.add(new StringField("author",potry.getPoet().getAuthor(), Field.Store.YES));

            indexWriter.addDocument(document);
        }

        indexWriter.commit();

        indexWriter.close();
    }

}
