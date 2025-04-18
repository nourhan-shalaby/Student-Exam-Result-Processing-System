package com.norhan.config;

import com.norhan.model.Student;
import com.norhan.model.StudentResult;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.repeat.RepeatStatus;

@Configuration
public class BatchConfiguration {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private DataSourceTransactionManager transactionManager;

    // Reader for Math CSV
    @Bean
public FlatFileItemReader<Student> mathReader() {
    FlatFileItemReader<Student> reader = new FlatFileItemReader<>();
    reader.setResource(new ClassPathResource("data/math.csv"));
    reader.setLinesToSkip(1); // Skip the header line
    reader.setLineMapper(new DefaultLineMapper<>() {{
        setLineTokenizer(new DelimitedLineTokenizer() {{
            setNames("studentId", "name", "subject", "marks");
        }});
        setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
            setTargetType(Student.class);
        }});
    }});
    return reader;
}

    // Reader for Science CSV
    @Bean
public FlatFileItemReader<Student> scienceReader() {
    FlatFileItemReader<Student> reader = new FlatFileItemReader<>();
    reader.setResource(new ClassPathResource("data/science.csv"));
    reader.setLinesToSkip(1); // Skip the header line
    reader.setLineMapper(new DefaultLineMapper<>() {{
        setLineTokenizer(new DelimitedLineTokenizer() {{
            setNames("studentId", "name", "subject", "marks");
        }});
        setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
            setTargetType(Student.class);
        }});
    }});
    return reader;
}

    // Reader for English CSV
 @Bean
public FlatFileItemReader<Student> englishReader() {
    FlatFileItemReader<Student> reader = new FlatFileItemReader<>();
    reader.setResource(new ClassPathResource("data/english.csv"));
    reader.setLinesToSkip(1); // Skip the header line
    reader.setLineMapper(new DefaultLineMapper<>() {{
        setLineTokenizer(new DelimitedLineTokenizer() {{
            setNames("studentId", "name", "subject", "marks");
        }});
        setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
            setTargetType(Student.class);
        }});
    }});
    return reader;
}
    // Processor to transform Student to StudentResult
    @Bean
    public ItemProcessor<Student, StudentResult> processor() {
        return student -> {
            // Validate input
            if (student.getName() == null || student.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Empty name for student ID: " + student.getStudentId());
            }
            if (student.getMarks() < 0 || student.getMarks() > 100) {
                throw new IllegalArgumentException("Invalid marks for student ID: " + student.getStudentId());
            }
            StudentResult result = new StudentResult();
            result.setStudentId(student.getStudentId());
            result.setName(student.getName().trim());
            result.setSubject(student.getSubject());
            result.setMarks(student.getMarks());
            result.setGrade(calculateGrade(student.getMarks()));
            return result;
        };
    }

    // Writer to save StudentResult to database
    @Bean
    public JdbcBatchItemWriter<StudentResult> writer() {
        return new JdbcBatchItemWriterBuilder<StudentResult>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO student_results (student_id, name, subject, marks, grade) VALUES (:studentId, :name, :subject, :marks, :grade)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
public Job processStudentDataJob() {
    return new JobBuilder("processStudentDataJob", jobRepository)
            .start(processMathDataStep())
            .next(processScienceDataStep())
            .next(processEnglishDataStep())
            .next(computeStatisticsStep())
            .build();
}

    // Steps for each subject
    @Bean
    public Step processMathDataStep() {
        return new StepBuilder("processMathDataStep", jobRepository)
                .<Student, StudentResult>chunk(100)
                .reader(mathReader())
                .processor(processor())
                .writer(writer())
                .transactionManager(transactionManager)
                .faultTolerant()
                .skip(IllegalArgumentException.class)
                .skipLimit(100)
                .build();
    }

    @Bean
    public Step processScienceDataStep() {
        return new StepBuilder("processScienceDataStep", jobRepository)
                .<Student, StudentResult>chunk(100)
                .reader(scienceReader())
                .processor(processor())
                .writer(writer())
                .transactionManager(transactionManager)
                .faultTolerant()
                .skip(IllegalArgumentException.class)
                .skipLimit(100)
                .build();
    }

    @Bean
    public Step processEnglishDataStep() {
        return new StepBuilder("processEnglishDataStep", jobRepository)
                .<Student, StudentResult>chunk(100)
                .reader(englishReader())
                .processor(processor())
                .writer(writer())
                .transactionManager(transactionManager)
                .faultTolerant()
                .skip(IllegalArgumentException.class)
                .skipLimit(100)
                .build();
    }

    // Step to compute advanced statistics
    @Bean
    public Step computeStatisticsStep() {
        return new StepBuilder("computeStatisticsStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
                    String[] subjects = {"Math", "Science", "English"};

                    for (String subject : subjects) {
                        // Compute average marks
                        Double avgMarks = jdbcTemplate.queryForObject(
                                "SELECT AVG(marks) FROM student_results WHERE subject = ?",
                                Double.class, subject
                        );
                        avgMarks = (avgMarks != null) ? avgMarks : 0.0;

                        // Compute pass rate
                        Integer totalStudents = jdbcTemplate.queryForObject(
                                "SELECT COUNT(*) FROM student_results WHERE subject = ?",
                                Integer.class, subject
                        );
                        Integer passedStudents = jdbcTemplate.queryForObject(
                                "SELECT COUNT(*) FROM student_results WHERE subject = ? AND marks >= 60",
                                Integer.class, subject
                        );
                        double passRate = (totalStudents != null && totalStudents > 0 && passedStudents != null)
                                ? (passedStudents * 100.0 / totalStudents) : 0.0;

                        // Compute grade distribution
                        Integer gradeACount = jdbcTemplate.queryForObject(
                                "SELECT COUNT(*) FROM student_results WHERE subject = ? AND grade = 'A'",
                                Integer.class, subject
                        );
                        gradeACount = (gradeACount != null) ? gradeACount : 0;
                        Integer gradeBCount = jdbcTemplate.queryForObject(
                                "SELECT COUNT(*) FROM student_results WHERE subject = ? AND grade = 'B'",
                                Integer.class, subject
                        );
                        gradeBCount = (gradeBCount != null) ? gradeBCount : 0;
                        Integer gradeCCount = jdbcTemplate.queryForObject(
                                "SELECT COUNT(*) FROM student_results WHERE subject = ? AND grade = 'C'",
                                Integer.class, subject
                        );
                        gradeCCount = (gradeCCount != null) ? gradeCCount : 0;
                        Integer gradeDCount = jdbcTemplate.queryForObject(
                                "SELECT COUNT(*) FROM student_results WHERE subject = ? AND grade = 'D'",
                                Integer.class, subject
                        );
                        gradeDCount = (gradeDCount != null) ? gradeDCount : 0;
                        Integer gradeFCount = jdbcTemplate.queryForObject(
                                "SELECT COUNT(*) FROM student_results WHERE subject = ? AND grade = 'F'",
                                Integer.class, subject
                        );
                        gradeFCount = (gradeFCount != null) ? gradeFCount : 0;

                        // Compute top students
                        String topStudents = jdbcTemplate.queryForObject(
                                "SELECT GROUP_CONCAT(name || ' (' || student_id || ')') FROM (SELECT name, student_id FROM student_results WHERE subject = ? ORDER BY marks DESC LIMIT 5)",
                                String.class, subject
                        );
                        topStudents = (topStudents != null) ? topStudents : "";

                        // Compute lowest students
                        String lowestStudents = jdbcTemplate.queryForObject(
                                "SELECT GROUP_CONCAT(name || ' (' || student_id || ')') FROM (SELECT name, student_id FROM student_results WHERE subject = ? ORDER BY marks ASC LIMIT 5)",
                                String.class, subject
                        );
                        lowestStudents = (topStudents != null) ? topStudents : "";

                        // Compute outliers
                        String outliers = jdbcTemplate.queryForObject(
                                "SELECT GROUP_CONCAT(student_id) FROM student_results WHERE subject = ? AND (marks < 30 OR marks > 95)",
                                String.class, subject
                        );
                        outliers = (outliers != null) ? outliers : "";

                        // Insert stats into advanced_stats table
                        jdbcTemplate.update(
                                "INSERT INTO advanced_stats (subject, average_marks, pass_rate, grade_a_count, grade_b_count, grade_c_count, grade_d_count, grade_f_count, top_students, lowest_students, outliers) " +
                                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                                subject, avgMarks, passRate, gradeACount, gradeBCount, gradeCCount, gradeDCount, gradeFCount, topStudents, lowestStudents, outliers
                        );
                    }
                    System.out.println("Statistics computation completed for all subjects.");
                    return RepeatStatus.FINISHED;
                })
                .transactionManager(transactionManager)
                .build();
    }

    private String calculateGrade(int marks) {
        if (marks >= 90) return "A";
        if (marks >= 80) return "B";
        if (marks >= 70) return "C";
        if (marks >= 60) return "D";
        return "F";
    }
}