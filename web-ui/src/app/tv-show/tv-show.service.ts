import {EventEmitter, Injectable} from '@angular/core';
import {Restangular} from 'ngx-restangular';
import {KeyObjectStorageService} from '../infrastructure/key-object-storage.service';
import {ConnectivityService} from '../infrastructure/connectivity.service';

@Injectable({providedIn: 'root'})
export class TvShowService {
  private tvShowsObservable = new EventEmitter();
  private tvShowEpisodes = {};
  private lastViewedTvShowEpisode = {};
  private restangular: Restangular;
  private keyObjectStorage: KeyObjectStorageService;
  private isOnline: boolean;
  constructor(restangular: Restangular, keyObjectStorage: KeyObjectStorageService, connectivityService: ConnectivityService) {
    this.restangular = restangular;
    this.keyObjectStorage = keyObjectStorage;
    this.isOnline = true;
    connectivityService.getIsOnlineObservable().subscribe(status => this.isOnline = status);
  }
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
    let tvShow;
    if (this.isOnline) {
      tvShow = await this.restangular.one('tv-show', id).get().toPromise();
      this.keyObjectStorage.save(`tv-show/${id}`, tvShow);
    } else {
      tvShow = this.keyObjectStorage.get(`tv-show/${id}`);
      if (!tvShow) {
        throw new Error('Don\'t remember such show. Go online, so can find information about it.');
      }
    }
    return tvShow;
  }
  async refreshTvShows() {
    let tvShows;
    if (this.isOnline) {
      tvShows = await this.restangular.all('tv-show').getList().toPromise();
      this.keyObjectStorage.save('tv-shows', tvShows);
    } else {
      tvShows = this.keyObjectStorage.get('tv-shows');
    }
    this.tvShowsObservable.emit(tvShows);
  }
  async refreshEpisodesOfTvShow(tvShow) {
    if (this.isOnline) {
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
          this.keyObjectStorage.save(`tv-show/${tvShow.id}/episodes`, episodes);
          this.getEpisodesOfTvShowObservable(tvShow).emit(episodes);
          if (lastViewedEpisode != null) {
            this.keyObjectStorage.save(`tv-show/${tvShow.id}/episodes/lastViewed`, lastViewedEpisode);
            this.getLastViewedEpisodeOfTvShowObservable(tvShow).emit(lastViewedEpisode);
          }
        }
      }
    } else {
      const episodes = this.keyObjectStorage.get(`tv-show/${tvShow.id}/episodes`);
      this.getEpisodesOfTvShowObservable(tvShow).emit(episodes);
      const lastViewedEpisode = this.keyObjectStorage.get(`tv-show/${tvShow.id}/episode/lastViewed`);
      this.getLastViewedEpisodeOfTvShowObservable(tvShow).emit(lastViewedEpisode);
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
