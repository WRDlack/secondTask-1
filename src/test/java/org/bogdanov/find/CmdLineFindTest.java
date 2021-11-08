package org.bogdanov.find;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CmdLineFindTest {


        @BeforeEach
        public void init() throws IOException {
            file1.createNewFile();

            file2.createNewFile();

            file3.getParentFile().mkdirs();
            file3.createNewFile();
        }

        @AfterEach
        public void clear() {
            file1.delete();

            file2.delete();

            file3.delete();
            file3.getParentFile().delete();
        }

        private final File currentDir = new File("").getAbsoluteFile();

        private final String filename1 = "testfile";
        private final File file1 = new File(currentDir, filename1);

        private final String dirname = "dir";
        private final String filename2 = "testfilename2.txt";
        private final File file2 = new File(currentDir, filename2);

        private final File file3 = new File(currentDir + File.separator + dirname, filename2);




        @Test
        public void findTest1() {
            Set<File> result = new Find(false, currentDir, new String[] { filename1 }).find();

            assertEquals(1, result.size());
            assertTrue(result.contains(file1));
        }

        @Test
        public void findTest2() {
            Set<File> result = new Find(false, currentDir, new String[] { filename2 }).find();

            assertEquals(1, result.size());
            assertTrue(result.contains(file2));
            assertTrue(!result.contains(file3));
        }

        @Test
        public void findTest1rec1() {
            Set<File> result = new Find(true, currentDir, new String[] { filename1 }).find();

            assertEquals(1, result.size());
            assertTrue(result.contains(file1));
        }

        @Test
        public void findTest1rec2() {
            Set<File> result = new Find(true, currentDir, new String[] { filename2 }).find();

            assertEquals(2, result.size());
            assertTrue(result.contains(file2));
            assertTrue(result.contains(file3));
        }

        @Test
        public void findTest3() {
            Set<File> result = new Find(false, currentDir, new String[] { "unexisted_file.file" }).find();

            assertEquals(0, result.size());
        }

        @Test
        public void findTest3rec() {
            Set<File> result = new Find(true, currentDir, new String[] { "unexisted_file.file" }).find();

            assertEquals(0, result.size());
        }

        @Test
        public void findTest4() {
            Set<File> result = new Find(false, currentDir, new String[] { filename1, filename2 }).find();

            assertEquals(2, result.size());
            assertTrue(result.contains(file1));
            assertTrue(result.contains(file2));

        }

        @Test
        public void findTest4rec() {
            Set<File> result = new Find(true, currentDir, new String[] { filename1, filename2 }).find();

            assertEquals(3, result.size());
            assertTrue(result.contains(file1));
            assertTrue(result.contains(file2));
            assertTrue(result.contains(file3));
        }
    }

