package com.linfq.learn.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * HelloHdfsTest.
 *
 * @author linfq
 * @date 2019/4/21 22:03
 */
public class HelloHdfsTest {



	/**
	 * URL例子程序.
	 *
	 * @throws IOException
	 */
	@Test
	public void testURL() throws IOException {
		URL url = new URL("https://www.baidu.com");
		InputStream in = url.openStream();
		IOUtils.copyBytes(in, System.out, 4096, true);
	}

	/**
	 * 使用URL读取HDFS文件
	 */
	@Test
	public void testURLHdfs() throws IOException {
		// 使URL对象能够识别hdfs协议
		URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
		URL url = new URL("hdfs://hadoop-master:9000/hello.txt");
		InputStream in = url.openStream();
		IOUtils.copyBytes(in, System.out, 4096, true);
	}

	/**
	 * 获取FileSystem.
	 *
	 * @return
	 * @throws IOException
	 */
	private FileSystem getFileSystem() throws IOException {
		// FileSystem
		Configuration conf = new Configuration();
		conf.set("fs.default.name", "hdfs://hadoop-master:9000");
//		conf.set("dfs.replication", "3");
		return FileSystem.get(conf);
	}

	/**
	 * FileSystem创建/删除/判断目录.
	 */
	@Test
	public void testFileSystem() throws IOException {
		FileSystem fileSystem = this.getFileSystem();
		// 创建
		boolean success = fileSystem.mkdirs(new Path("/linfq"));
		Assert.assertTrue(success);
		// 判断文件或目录是否存在
		success = fileSystem.exists(new Path("/linfq"));
		Assert.assertTrue(success);
		// 删除
		success = fileSystem.delete(new Path("/linfq"), true);
		Assert.assertTrue(success);
		// 再次判断是否删除成功
		success = fileSystem.exists(new Path("/linfq"));
		Assert.assertFalse(success);
	}

	/**
	 * 测试上传本地文件.
	 */
	@Test
	public void testUploadLocalFile() throws IOException {
		FileSystem fileSystem = this.getFileSystem();
		// 上传windows文件到hdfs
		FileInputStream fis = new FileInputStream("E:\\Program Files\\hadoop-2.7.3\\etc\\hadoop\\core-site.xml");
		FSDataOutputStream out = fileSystem.create(new Path("/test1.data"), true);
		IOUtils.copyBytes(fis, out, 4096, true);
	}

	/**
	 * 测试原始方法上传.
	 */
	@Test
	public void testUploadPremeval() throws IOException {
		FileSystem fileSystem = this.getFileSystem();
		// 原始方式上传，可以实现上传进度
		FileInputStream fis = new FileInputStream("D:\\.m2\\settings.xml");
		FSDataOutputStream out = fileSystem.create(new Path("/test2.data"), true);
		byte[] buf = new byte[4096];
		int len = fis.read(buf);
		while (len != -1) {
			out.write(buf, 0, len);
			len = fis.read(buf);
		}
		fis.close();
		out.close();
	}

	/**
	 * 测试列举目录下的信息.
	 */
	@Test
	public void testListDir() throws IOException {
		FileSystem fileSystem = this.getFileSystem();
		// 列举目录下的信息
		FileStatus[] statuses = fileSystem.listStatus(new Path("/"));
		System.out.println(statuses.length);
		for (FileStatus status : statuses) {
			// 文件路径
			System.out.println(status.getPath());
			// 权限信息
			System.out.println(status.getPermission());
			// 备份数量
			System.out.println(status.getReplication());
		}
	}
}
