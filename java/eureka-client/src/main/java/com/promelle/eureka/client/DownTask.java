package com.promelle.eureka.client;

import io.dropwizard.servlets.tasks.Task;

import java.io.PrintWriter;

import com.google.common.collect.ImmutableMultimap;

public class DownTask extends Task {
    private EurekaInstance eurekaInstance;

    public DownTask(EurekaInstance eurekaInstance) {
        super("down");
        this.eurekaInstance = eurekaInstance;
    }

    @Override
    public void execute(ImmutableMultimap<String, String> parameters, PrintWriter writer) throws Exception {
        eurekaInstance.markAsDown();
        writer.write("DOWN");
    }
}
