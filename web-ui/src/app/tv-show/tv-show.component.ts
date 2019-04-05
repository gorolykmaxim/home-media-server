import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {MatBottomSheet, MatDialog} from '@angular/material';
import {ConfirmationDialogComponent} from '../common/confirmation-dialog.component';
import {EditTvShowPopupComponent} from './edit-tv-show-popup.component';
import {AddEpisodePopupComponent} from './add-episode-popup.component';
import {TvShowService} from './tv-show.service';
import {ErrorService} from '../error/error.service';

@Component({
  selector: 'app-tv-show',
  templateUrl: './tv-show.component.html'
})
export class TvShowComponent implements OnInit, OnDestroy {
  private tvShow: any = {};
  private episodes = [];
  private lastViewedEpisode: any;
  constructor(private route: ActivatedRoute, private tvShowService: TvShowService, private dialog: MatDialog, private router: Router,
              private bottomSheet: MatBottomSheet, private errorService: ErrorService) {}
  async ngOnInit(): Promise<void> {
    try {
      this.errorService.registerFailingComponent(TvShowComponent);
      this.lastViewedEpisode = null;
      const tvShowId = this.route.snapshot.paramMap.get('id');
      this.tvShow = await this.tvShowService.getTvShowById(tvShowId);
      this.tvShowService.getEpisodesOfTvShowObservable(this.tvShow).subscribe(episodes => this.episodes = episodes);
      this.tvShowService.getLastViewedEpisodeOfTvShowObservable(this.tvShow).subscribe(lastViewed => this.lastViewedEpisode = lastViewed);
      await this.tvShowService.refreshEpisodesOfTvShow(this.tvShow);
    } catch (e) {
      this.errorService.notifyAbout(e, TvShowComponent);
    }
  }
  editShow() {
    this.bottomSheet.open(EditTvShowPopupComponent, {data: {tvShow: this.tvShow}});
  }
  deleteShow() {
    this.dialog.open(ConfirmationDialogComponent, {
      data: {
        title: `Delete ${this.tvShow.name}`,
        message: 'Are you sure you want to delete this show?'
      }
    }).afterClosed().subscribe(async result => {
      if (result) {
        try {
          await this.tvShowService.removeTvShow(this.tvShow);
          this.router.navigateByUrl('/tv-show');
        } catch (e) {
          this.errorService.notifyAbout(e, TvShowComponent);
        }
      }
    });
  }
  addEpisode() {
    this.bottomSheet.open(AddEpisodePopupComponent, {data: {tvShow: this.tvShow}});
  }
  deleteEpisode(episode) {
    this.dialog.open(ConfirmationDialogComponent, {
      data: {
        title: `Delete ${episode.name}`,
        message: 'Are you sure you want to delete this episode?'
      }
    }).afterClosed().subscribe(async result => {
      if (result) {
        try {
          await this.tvShowService.deleteEpisode(this.tvShow, episode);
        } catch (e) {
          this.errorService.notifyAbout(e, TvShowComponent);
        }
      }
    });
  }
  ngOnDestroy(): void {
    this.errorService.unregisterFailingComponent(TvShowComponent);
  }
}
