package com.lucene.test;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * 高亮查询
 *
 * Created by Administrator on 2017/12/6.
 */
public class HighLightingTest {


    /**
     * 测试高亮
     */
    @Test
    public void testHL() throws IOException, InvalidTokenOffsetsException, ParseException {

        String keyword ="鹅";

        // keyword = "床前明 光";

        keyword = "天生我材必有用";

        FSDirectory fsDirectory = FSDirectory.open(Paths.get("F://index/demo5"));

        IndexReader indexReader = DirectoryReader.open(fsDirectory);

        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        /*Term term = new Term("content", keyword);

        Query query = new TermQuery(term);*/

        QueryParser queryParser = new QueryParser("content", new StandardAnalyzer());

        Query query = queryParser.parse(keyword);

        // ==================================

        //1. 创建高亮器对象
        Scorer scorer = new QueryScorer(query);

        Formatter formatter = new SimpleHTMLFormatter("<span style='color:red'>","</span>");

        Highlighter highlighter = new Highlighter(formatter,scorer);
        // ==================================

        TopDocs topDocs = indexSearcher.search(query, 10);

        System.out.println("符合条件的记录数:"+topDocs.totalHits);

        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        for (ScoreDoc scoreDoc : scoreDocs) {

            System.out.println("文档的得分:"+scoreDoc.score);

            int docID =scoreDoc.doc;

            Document document = indexReader.document(docID);

            // 获取最佳内容的片段
            // 第一个参数: 分词器 第二个参数:域名  第三个参数: 域的原始内容
            String bestFragment = highlighter.getBestFragment(new StandardAnalyzer(), "content", document.get("content"));

            System.out.println(document.get("id") + " | " + document.get("title") +" | "+bestFragment+ " | "+document.get("author"));

        }
        indexReader.close();
    }
}
