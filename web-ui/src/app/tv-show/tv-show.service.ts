import {EventEmitter, Injectable} from '@angular/core';
import {Restangular} from 'ngx-restangular';

@Injectable({providedIn: 'root'})
export class TvShowService {
  private tvShowsObservable = new EventEmitter();
  private tvShowEpisodes = {};
  private lastViewedTvShowEpisode = {};
  constructor(private restangular: Restangular) {}
  getTvShowsObservable() {
    return this.tvShowsObservable;
  }
  getEpisodesOfTvShowObservable(tvShow) {
    let episodesObservable = this.tvShowEpisodes[tvShow.id];
    if (!episodesObservable) {
      episodesObservable = this.tvShowEpisodes[tvShow.id] = new EventEmitter();
    }
    return episodesObservable;
  }
  getLastViewedEpisodeOfTvShowObservable(tvShow) {
    let lastViewedEpisodeObservable = this.lastViewedTvShowEpisode[tvShow.id];
    if (!lastViewedEpisodeObservable) {
      lastViewedEpisodeObservable = this.lastViewedTvShowEpisode[tvShow.id] = new EventEmitter();
    }
    return lastViewedEpisodeObservable;
  }
  async getTvShowById(id) {
    return await this.restangular.one('tv-show', id).get().toPromise();
  }
  async refreshTvShows() {
    const tvShows = await this.restangular.all('tv-show').getList().toPromise();
    this.tvShowsObservable.emit(tvShows);
  }
  async refreshEpisodesOfTvShow(tvShow) {
    const episodes = await this.restangular.one('tv-show', tvShow.id).all('episode').getList().toPromise();
    if (episodes && episodes.length > 0) {
      let views = await this.restangular.all('episode')
        .all('view').getList({episodeName: episodes.map(episode => episode.name)}).toPromise();
      if (views && views.length > 0) {
        views = views.map(view => view.episodeName);
        let lastViewedEpisode = null;
        for (const episode of episodes) {
          episode.viewed = views.indexOf(episode.name) >= 0;
          if (episode.viewed) {
            lastViewedEpisode = episode;
          }
        }
        this.getEpisodesOfTvShowObservable(tvShow).emit(episodes);
        if (lastViewedEpisode != null) {
          this.getLastViewedEpisodeOfTvShowObservable(tvShow).emit(lastViewedEpisode);
        }
      }
    }
  }
  async addTvShow(tvShow) {
    return await this.restangular.all('tv-show').post(tvShow).toPromise();
  }
  async setThumbnailOfShow(tvShow, thumbnail) {
    await this.restangular.one('tv-show', tvShow.id).customPUT({thumbnail}, 'thumbnail').toPromise();
  }
  async removeTvShow(tvShow) {
    await tvShow.remove().toPromise();
  }
  async updateTvShow(tvShow) {
    await tvShow.put().toPromise();
  }
  async addEpisode(tvShow, episode) {
    await this.restangular.one('tv-show', tvShow.id).all('episode').post(episode).toPromise();
    await this.refreshEpisodesOfTvShow(tvShow);
  }
  async deleteEpisode(tvShow, episode) {
    await this.restangular.one('tv-show', tvShow.id).customDELETE('episode', {name: episode.name}).toPromise();
    await this.refreshEpisodesOfTvShow(tvShow);
  }
}
