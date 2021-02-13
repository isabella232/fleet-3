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
import io.linuxserver.fleet.v2.Utils;
import io.linuxserver.fleet.v2.client.docker.DockerApiClient;
import io.linuxserver.fleet.v2.client.docker.DockerImageNotFoundException;
import io.linuxserver.fleet.v2.key.ImageKey;
import io.linuxserver.fleet.v2.key.RepositoryKey;
import io.linuxserver.fleet.v2.thread.AsyncTaskDelegate;
import io.linuxserver.fleet.v2.types.docker.DockerImage;
import io.linuxserver.fleet.v2.types.docker.DockerTag;

import java.util.List;

public class DockerApiDelegate implements AsyncTaskDelegate {

    private final FleetAppController controller;
    private final DockerApiClient    apiClient;

    public DockerApiDelegate(final FleetAppController controller,
                             final DockerApiClient dockerApiClient) {
        this.controller = controller;
        this.apiClient  = Utils.ensureNotNull(dockerApiClient);
    }

    public final boolean isRepositoryValid(final String repositoryName) {
        return apiClient.isRepositoryValid(repositoryName);
    }

    public final List<DockerImage> getImagesForRepository(final RepositoryKey repositoryKey) {
        return apiClient.fetchAllImages(repositoryKey.getName());
    }

    public final DockerImage getCurrentImageView(final ImageKey imageKey) {

        final DockerImage dockerImage = apiClient.fetchImage(imageKey.getAsRepositoryAndImageName());
        if (null == dockerImage) {
            throw new DockerImageNotFoundException("Image " + imageKey.getAsRepositoryAndImageName() + " was not found upstream.");
        }

        final List<DockerTag> allImageTags = apiClient.fetchImageTags(imageKey.getAsRepositoryAndImageName());
        allImageTags.forEach(dockerImage::addTag);

        return dockerImage;
    }

    @Override
    public FleetAppController getController() {
        return controller;
    }
}
