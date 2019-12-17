/*
 * Copyright (c)  2019 LinuxServer.io
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.linuxserver.fleet.v2.client.docker.queue;

import io.linuxserver.fleet.core.FleetAppController;
import io.linuxserver.fleet.v2.key.ImageKey;
import io.linuxserver.fleet.v2.types.docker.DockerImage;

public class DockerImageUpdateResponse implements AsyncDockerApiResponse {

    private final FleetAppController controller;
    private final ImageKey           imageKey;
    private final DockerImage        latestImage;

    public DockerImageUpdateResponse(final FleetAppController controller,
                                     final ImageKey imageKey,
                                     final DockerImage latestImage) {
        this.controller  = controller;
        this.imageKey    = imageKey;
        this.latestImage = latestImage;
    }

    protected final FleetAppController getController() {
        return controller;
    }

    @Override
    public void handleDockerApiResponse() {
        controller.getRepositoryManager().applyImageUpdate(imageKey, latestImage);
    }

    @Override
    public void handleResponse() {
        handleDockerApiResponse();
    }

    @Override
    public final String toString() {
        return imageKey.toString();
    }
}
