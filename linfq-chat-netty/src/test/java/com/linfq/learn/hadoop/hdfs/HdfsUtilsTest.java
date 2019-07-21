package com.linfq.learn.hadoop.hdfs;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * hdfs工具类测试类.
 *
 * @author linfq
 * @date 2019/4/11 20:43
 */
public class HdfsUtilsTest {

	@Test
	public void list() throws IOException {
		HdfsUtils hdfsUtils = new HdfsUtils("hdfs://hadoop-master:9000");
		System.out.println(hdfsUtils.list("/"));
	}

	@Test
	public void text() {
	}

	@Test
	public void put() {
	}

	@Test
	public void delete() {
	}

	@Test
	public void makeDir() {
	}
}