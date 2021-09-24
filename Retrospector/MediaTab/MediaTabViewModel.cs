using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.EntityFrameworkCore;
using Retrospector.DataStorage.Interfaces;
using Retrospector.DataStorage.Models;
using Retrospector.MediaTab.Interfaces;
using Retrospector.Setup;

namespace Retrospector.MediaTab
{
    public class MediaTabViewModel : IMediaTab
    {
        private readonly Func<IDatabaseContext> _contextFactory;

        public Media Media { get; set; }
        public string Header { get; set; } = "Media";
        public ICollection<string> Categories { get; set; }
        public ICollection<MediaType> MediaTypes { get; set; }

        public MediaTabViewModel(Configuration config, Func<IDatabaseContext> contextFactory)
        {
            _contextFactory = contextFactory;
            Categories = config.Categories;
            MediaTypes = Enum.GetValues<MediaType>();
        }

        public void Load(int mediaId)
        {
            var context = _contextFactory.Invoke();
            Media = context
                .Media
                .Include(m => m.Reviews)
                .Include(m => m.Factoids)
                .FirstOrDefault(m => m.Id == mediaId);
        }

        public void New()
        {
            Media = new Media();
        }
    }
}