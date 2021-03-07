using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Retrospector.DataStorage.Factoids.Entities;
using Retrospector.DataStorage.Reviews.Entities;

namespace Retrospector.DataStorage.Medias.Entities
{
    [Table("Media")]
    public class MediaEntity
    {
        [Key]
        public int Id { get; set; }
        public string Title { get; set; }

        public string SeasonId { get; set; }

        public string EpisodeId { get; set; }

        public string Creator { get; set; }

        public string Category { get; set; }

        public MediaTypeEntity Type { get; set; }

        public string Description { get; set; }

        public DateTime CreatedDate { get; set; }

        public ICollection<ReviewEntity> Reviews { get; set; } = new List<ReviewEntity>();

        public ICollection<FactoidEntity> Factoids { get; set; } = new List<FactoidEntity>();
    }
}