using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Retrospector.DataStorage.Models
{
    [Table("Media")]
    public class Media
    {
        [Key]
        public int Id { get; set; }
        public string Title { get; set; }

        public string SeasonId { get; set; }

        public string EpisodeId { get; set; }

        public string Creator { get; set; }

        public string Category { get; set; }

        public MediaType Type { get; set; }

        public string Description { get; set; }

        public DateTime CreatedDate { get; set; }

        public virtual ICollection<Review> Reviews { get; set; } = new List<Review>();

        public virtual ICollection<Factoid> Factoids { get; set; } = new List<Factoid>();
    }
}