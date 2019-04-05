import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {TvShowService} from './tv-show.service';
import {ThumbnailService} from './thumbnail.service';
import {ErrorService} from '../error/error.service';

@Component({
  selector: 'app-thumbnail-pick',
  templateUrl: './thumbnail-pick.component.html'
})
export class ThumbnailPickComponent implements OnInit, OnDestroy {
  private thumbnails: string[];
  private tvShow: any;
  private loadingImages: boolean;
  constructor(private tvShowService: TvShowService, private thumbnailService: ThumbnailService, private route: ActivatedRoute,
              private router: Router, private errorService: ErrorService) {}
  async ngOnInit() {
    this.loadingImages = true;
    this.errorService.registerFailingComponent(ThumbnailPickComponent);
    try {
      const tvShowId = this.route.snapshot.paramMap.get('id');
      this.tvShow = await this.tvShowService.getTvShowById(tvShowId);
      const thumbnails = await this.thumbnailService.findThumbnailBySearchTerm(this.tvShow.name);
      this.thumbnails = thumbnails.map(thumbnail => thumbnail.uri);
    } catch (e) {
      this.errorService.notifyAbout(e, ThumbnailPickComponent);
    } finally {
      this.loadingImages = false;
    }
  }
  async chooseThumbnail(index) {
    try {
      const thumbnail = this.thumbnails[index];
      await this.tvShowService.setThumbnailOfShow(this.tvShow, thumbnail);
      this.router.navigateByUrl(`/tv-show/${this.tvShow.id}`);
    } catch (e) {
      this.errorService.notifyAbout(e, ThumbnailPickComponent);
    }
  }
  ngOnDestroy(): void {
    this.errorService.unregisterFailingComponent(ThumbnailPickComponent);
  }
}
