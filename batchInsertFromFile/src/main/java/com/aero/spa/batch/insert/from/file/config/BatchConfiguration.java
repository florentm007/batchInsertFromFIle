package com.aero.spa.batch.insert.from.file.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.aero.spa.batch.insert.from.file.model.User;

@Configuration
public class BatchConfiguration {

	private static final int LINE_TO_SKIP_IN_CSV = 1;
	private static final String CSV_FILE_NAME = "csvFile";
	
	@Bean
	public Job insertIntoDatabaseFromFileJob(Step insertFromFileStep, JobBuilderFactory jobBuilderFactory) {

		return jobBuilderFactory.get("insertIntoDatabaseFromFileJob").incrementer(new RunIdIncrementer())
				.start(insertFromFileStep).build();
	}

	@Bean
	public Step insertFromFileStep(StepBuilderFactory stepBuilderFactory, ItemReader<User> itemReader,
			ItemProcessor<User, User> itemProcessor, ItemWriter<User> itemWriter) {
		return stepBuilderFactory.get("insertFromFileStep")
				.<User,User>chunk(100)
				.reader(itemReader)
				.processor(itemProcessor)
				.writer(itemWriter)
				.build();
	}
	
	@Bean
	public FlatFileItemReader<User> itemReader(@Value("${input}") Resource resource) {
		FlatFileItemReader<User> flatFileItemReader = new FlatFileItemReader();
		flatFileItemReader.setResource(resource);
		flatFileItemReader.setName(CSV_FILE_NAME);
		flatFileItemReader.setLinesToSkip(LINE_TO_SKIP_IN_CSV);
		flatFileItemReader.setLineMapper(lineMapper());
		return null;
	}
	
    @Bean
    public LineMapper<User> lineMapper() {

        DefaultLineMapper<User> defaultLineMapper = new DefaultLineMapper();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[]{"userIdentifer", "name", "email"});

        BeanWrapperFieldSetMapper<User> fieldSetMapper = new BeanWrapperFieldSetMapper();
        fieldSetMapper.setTargetType(User.class);

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
}
}
